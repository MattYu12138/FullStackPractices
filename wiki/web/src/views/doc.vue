<template>
  <a-layout>
    <a-row>
      <a-col :span="6">
        <a-tree
            v-if="array2Tree.level1.length > 0"
            :tree-data="array2Tree.level1"
            @select="onSelect"
            :field-names="{ title: 'name', key: 'id', value: 'id' }"
            :default-expand-all-rows="true"
        >
        </a-tree>
      </a-col>
      <a-col :span="18">
      </a-col>
    </a-row>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, createVNode, onMounted, shallowRef, ref, reactive, onBeforeUnmount} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool'
import {useRoute} from "vue-router";
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Modal } from 'ant-design-vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'


export default defineComponent({
  name: 'Doc',
  components: { Editor, Toolbar },
  setup() {

    // const editorRef = shallowRef();
    // const valueHtml = ref('<p>hello</p>');
    //
    // const toolbarConfig = {}
    // const editorConfig = { placeholder: '请输入内容...' }

    const gettingDocs = reactive({
      doc: [],
      pagination: {current: 1, pageSize: 5, total: 0}
    });

    const model = reactive({
      expandedRowKeys:[] as number[],
      visible: false,
      loading: false,
      route: useRoute(),
    })

    const array2Tree = reactive({
      level1: [] as any[],
      // SelectedData: [] as any[],
    })

    // const postingDocs = reactive({
    //   doc: {},
    //   name : "",
    //   id: 0,
    // })

    // const setDisable = (treeSelectedData: any, id: any) => {
    //   // 遍历所有节点找到当前节点id
    //   for(let i = 0; i < treeSelectedData.length; i ++){
    //     const node = treeSelectedData[i];
    //     if(node.id === id) {
    //       // 找到之后设置为disable
    //       console.log("disabled", node);
    //       node.disabled = true;
    //       // 遍历找到节点的所有子节点，如果找到，设置为disable
    //       const children = node.children;
    //       if (Tool.isNotEmpty(children)) {
    //         for (let j = 0; j < children.length; j++) {
    //           setDisable(children, children[j].id);
    //         }
    //       }
    //     }else {
    //       // 如果没有找到当前节点，则道其他子节点中寻找当前节点
    //       const children = node.children;
    //       if (Tool.isNotEmpty(children)) {
    //         setDisable(children, id);
    //       }
    //     }
    //   }
    // }

    // const ids: Array<string> = [];
    // const name: Array<string> = [];


    // const getDeletedItemsInfo = (treeSelectedData: any, id: any) => {
    //   // 遍历所有节点找到当前节点id
    //   for(let i = 0; i < treeSelectedData.length; i ++){
    //     const node = treeSelectedData[i];
    //     if(node.id === id) {
    //       // 找到之后设置为disable
    //       ids.push(id);
    //       name.push(node.name);
    //
    //       // 遍历找到节点的所有子节点
    //       const children = node.children;
    //       if (Tool.isNotEmpty(children)) {
    //         for (let j = 0; j < children.length; j++) {
    //           getDeletedItemsInfo(children, children[j].id);
    //         }
    //       }
    //     }else {
    //       // 如果没有找到当前节点，则道其他子节点中寻找当前节点
    //       const children = node.children;
    //       if (Tool.isNotEmpty(children)) {
    //         getDeletedItemsInfo(children, id);
    //       }
    //     }
    //   }
    // }

    // const handleQueryContent = () => {
    //   axios.get("doc/find-content/" + postingDocs.doc.id).then((response) => {
    //     const data = response.data;
    //     if(data.success){
    //       valueHtml.value = data.content;
    //     }else{
    //       message.error(data.message);
    //     }
    //   })
    // }

    // /*
    // * edit
    // * */
    // const edit = (record: any) => {
    //   valueHtml.value = "";
    //   model.visible = true;
    //   postingDocs.doc = Tool.copy(record);
    //   handleQueryContent();
    //
    //   array2Tree.SelectedData = Tool.copy(array2Tree.level1);
    //   setDisable(array2Tree.SelectedData, record.id);
    //
    //   array2Tree.SelectedData.unshift({id:0, name:'无'});
    // }
    // /*
    // * add
    // * */
    // const add = () => {
    //
    //   valueHtml.value = "";
    //   model.visible = true;
    //   postingDocs.doc = {
    //     ebookId: model.route.query.ebookId,
    //   };
    //   // handleQueryContent();
    //
    //   array2Tree.SelectedData = Tool.copy(array2Tree.level1);
    //   array2Tree.SelectedData.unshift({id:0, name:'无'});
    // }


    const handleQuery = () => {
      model.loading = true;
      array2Tree.level1 = [];
      axios.get("doc/all").then((response) => {
        model.loading = false;
        const data = response.data;
        if(data.success){
          gettingDocs.doc = data.content;
          console.log("original: " , gettingDocs.doc);

          array2Tree.level1 = [];
          array2Tree.level1 = Tool.array2Tree(gettingDocs.doc, 0);
          console.log("树形结构: " , array2Tree.level1);

        }else{
          message.error(data.message);
        }
      })
    }




    // const handleDelete = (id: number) => {
    //   ids.length = 0;
    //   name.length = 0;
    //   getDeletedItemsInfo(array2Tree.level1, id);
    //   axios.delete("doc/delete/" + ids.join(",")).then((response) => {
    //     const data = response.data
    //     if (data.success) {
    //       handleQuery();
    //     }
    //   });
    // };
    //
    //
    // const handleSave = () => {
    //   model.loading = true;
    //   postingDocs.doc.content = valueHtml;
    //   axios.post("doc/save", postingDocs.doc).then((response) => {
    //     model.loading = false;
    //     const data = response.data
    //     if (data.success) {
    //       message.success("Save Success");
    //       handleQuery();
    //     }else{
    //       message.error(data.message);
    //     }
    //   })
    // }

    // const onExpand = (expanded: boolean, record: any) => {
    //   if (expanded) {
    //     if (!model.expandedRowKeys.includes(record.id)) {
    //       model.expandedRowKeys.push(record.id);
    //     }
    //   } else {
    //     model.expandedRowKeys = model.expandedRowKeys.filter(id => id !== record.id);
    //   }
    // };

    //
    // const showConfirm = (id:number) => {
    //   ids.length = 0;
    //   name.length = 0;
    //   getDeletedItemsInfo(array2Tree.level1, id);
    //   Modal.confirm({
    //     title: () => '删除确认',
    //     icon: () => createVNode(ExclamationCircleOutlined),
    //     content: () => `你将删除文档及其子文档（${name.join(',')}），确定删除吗？`,
    //     onOk() {
    //       handleDelete(id);
    //     },
    //     // eslint-disable-next-line @typescript-eslint/no-empty-function
    //     onCancel() {},
    //   });
    // };
    //
    // onBeforeUnmount(() => {
    //   const editor = editorRef.value
    //   if (editor == null) return
    //   editor.destroy()
    // })
    //
    // const handleCreated = (editor:any) => {
    //   editorRef.value = editor // 记录 editor 实例，重要！
    // }

    //
    // const columns = [
    //   { title: '名称', dataIndex: 'name', key: 'name' },
    //   {title: '操作', key: 'action',},
    // ];


    onMounted(() => {
      handleQuery();
    });

    return {
      gettingDocs,
      // postingDocs,
      // onExpand,
      array2Tree,
      // columns,
      handleQuery,
      // showConfirm,


      model,
      // handleSave,
      // edit,
      // add,
      // handleDelete,
      //
      // editorRef,
      // valueHtml,
      // mode: 'default', // 或 'simple'
      // toolbarConfig,
      // editorConfig,
      // handleCreated,
    }
  }
});
</script>


<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>

