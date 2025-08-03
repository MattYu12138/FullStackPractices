<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="array2Tree.level1.length === 0">对不起，找不到相关文档</h3>
    <a-row>
      <a-col :span="6">
        <a-tree
            v-if="array2Tree.level1.length > 0"
            :tree-data="array2Tree.level1"
            @select="onSelect"
            :field-names="{ title: 'name', key: 'id', value: 'id' }"
            :defaultExpandAll="true"
            :defaultSelectedKeys="defaultSelectedKeys"
        >
        </a-tree>
      </a-col>
      <a-col :span="18">
        <div>
          <h2>{{doc.name}}</h2>
          <div>
            <span>Read: {{doc.viewCount}}</span> &nbsp; &nbsp;
            <span>Vote: {{doc.voteCount}}</span>
          </div>
          <a-divider style="height: 2px;  background-color: #9999cc"/>
        </div>
        <div class="wangEditor" v-html="valueHtml"></div>
        <div class="vote-div">
          <a-button type="primary" shape="round" size="small" @click="vote">
            <template #icon><LikeOutlined/> &nbsp;thumbs up: {{doc.voteCount}}</template>
          </a-button>
        </div>
      </a-col>
    </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool'
import {useRoute} from "vue-router";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'


export default defineComponent({
  name: 'Doc',
  components: { Editor, Toolbar },
  setup() {

    // const editorRef = shallowRef();
    const valueHtml = ref('');
    //
    // const toolbarConfig = {}
    // const editorConfig = { placeholder: '请输入内容...' }

    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

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

    const doc = ref();
    doc.value = {};

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

    const handleQueryContent = (id:number) => {
      axios.get("doc/find-content/" + id).then((response) => {
        const data = response.data;
        if(data.success){
          valueHtml.value = data.content;
        }else{
          message.error(data.message);
        }
      })
    }

    const onSelect = (selectedKeys: any, info: any)=>{
      console.log('selected ', selectedKeys, info);
      if(Tool.isNotEmpty(selectedKeys)){
        doc.value = info.selectedNodes[0];
        handleQueryContent(selectedKeys[0]);
      }
    }

    const vote = () => {
      axios.get('/doc/vote/' + doc.value.id).then((response) =>{
        const data = response.data;
        if(data.success){
          doc.value.voteCount++;
        }else{
          message.error(data.message);
        }
      })
    }

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
      axios.get("doc/all/" + model.route.query.ebookId).then((response) => {
        model.loading = false;
        const data = response.data;
        if(data.success){
          gettingDocs.doc = data.content;
          console.log("original: " , gettingDocs.doc);

          array2Tree.level1 = [];
          array2Tree.level1 = Tool.array2Tree(gettingDocs.doc, 0);
          console.log("树形结构: " , array2Tree.level1);

          if(Tool.isNotEmpty(array2Tree.level1)){
            defaultSelectedKeys.value = [array2Tree.level1[0].id]
            handleQueryContent(array2Tree.level1[0].id)
            // 初始显示文档信息
            doc.value = array2Tree.level1[0];
          }

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
      doc,
      vote,
      gettingDocs,
      // postingDocs,
      // onExpand,
      array2Tree,
      // columns,
      handleQuery,
      // showConfirm,
      onSelect,
      defaultSelectedKeys,


      model,
      // handleSave,
      // edit,
      // add,
      // handleDelete,
      //
      // editorRef,
      valueHtml,
      // mode: 'default', // 或 'simple'
      // toolbarConfig,
      // editorConfig,
      // handleCreated,
    }
  }
});
</script>


<style>
.wangEditor {
  border: 3px solid #ccc;
  border-radius: 5px;
  padding: 0 10px;
  margin-top: 20px;
  overflow-x: auto;
}

.wangEditor p,
.wangEditor li {
  white-space: pre-wrap; /* 保留空格 */
}

.wangEditor blockquote {
  border-left: 8px solid #d0e5f2;
  padding: 10px 10px;
  margin: 10px 0;
  background-color: #f1f1f1;
}

.wangEditor code {
  font-family: monospace;
  background-color: #eee;
  padding: 3px;
  border-radius: 3px;
}
.wangEditor pre>code {
  display: block;
  padding: 10px;
}

.wangEditor table {
  border-collapse: collapse;
}
.wangEditor td,
.wangEditor th {
  border: 1px solid #ccc;
  min-width: 50px;
  height: 20px;
}
.wangEditor th {
  background-color: #f1f1f1;
}

.wangEditor ul,
.wangEditor ol {
  padding-left: 20px;
}

.wangEditor input[type="checkbox"] {
  margin-right: 5px;
}
</style>