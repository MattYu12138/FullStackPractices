<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form layout="inline" :model="postingDocs" style="margin-bottom: 2vw">
            <a-form-item>
              <a-button type="primary" @click="handleQuery()">
                查询
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()">
                新增
              </a-button>
            </a-form-item>
          </a-form>
          <a-table
              v-if="array2Tree.level1.length > 0"
              :columns="columns"
              :data-source="array2Tree.level1"
              :loading="model.loading"
              :pagination="false"
              :row-key="(record:any) => record.id"
              :default-expand-all-rows="true"
              size="small"
          >

            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'cover'">
                <img :src="record.cover" alt="cover" style="width: 60px"/>
              </template>

              <!-- 操作 -->
              <template v-else-if="column.key === 'action'">
                <a-space>
                  <a-button type="primary" @click="edit(record)" size="small">编辑</a-button>
                  <a-popconfirm
                      title="Are you sure delete this task?"
                      ok-text="Yes"
                      cancel-text="No"
                      @confirm="showConfirm(record.id)"
                  >
                    <a-button type="default" size="small">删除</a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline"
                    :model="postingDocs.doc"
                    style="margin-bottom: 2vw"
            >
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  save
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="postingDocs.doc" :label-col="{ span: 6 }">
            <a-form-item>
              <a-input v-model:value="postingDocs.doc.name" placeholder="Name"></a-input>
            </a-form-item>

            <a-form-item>
              <a-tree-select
                  v-model:value="postingDocs.doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="array2Tree.SelectedData"
                  placeholder="请点击新增或编辑选择父文档"
                  tree-default-expand-all
                  :field-names="{ label: 'name', key: 'id', value: 'id' }"
              >
              </a-tree-select>
            </a-form-item>

            <a-form-item aria-placeholder="sort">
              <a-input-number v-model:value="postingDocs.doc.sort" :min="0" style="width: 100%" placeholder="Sort"/>
            </a-form-item>

            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined/> 内容预览
              </a-button>
            </a-form-item>

            <a-form-item >
              <div style="border: 1px solid #ccc; padding: 10px; width: 100%;">
                <Toolbar
                    style="border-bottom: 1px solid #ccc; margin-bottom: 5px;"
                    :editor="editorRef"
                    :defaultConfig="toolbarConfig"
                    :mode="'default'"
                />
                <Editor
                    style="height: 400px; overflow-y: auto;"
                    v-model="valueHtml"
                    :defaultConfig="editorConfig"
                    :mode="'default'"
                    @onCreated="handleCreated"
                />
              </div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="90vw" placement="right" :closable="false" :visible="drawer.visible" @close="onDrawerClose">
        <div class="wangEditor" v-html="drawer.previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>
<!--  <a-modal-->
<!--      title="文档表单"-->
<!--      v-model:open="model.visible"-->
<!--      :confirm-loading="model.loading"-->
<!--      :width="'80vw'"-->
<!--      @ok="handleSave"-->
<!--  >-->
<!--  </a-modal>-->
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
  name: 'AdminDoc',
  components: { Editor, Toolbar },
  setup() {

    const editorRef = shallowRef();
    const valueHtml = ref('<p>请点击章节进行预览</p>');


    const toolbarConfig = {}
    const editorConfig = { placeholder: '请输入内容...' }

    const gettingDocs = reactive({
      doc: [],
      pagination: {current: 1, pageSize: 5, total: 0}
    });

    const drawer = reactive({
      visible: false,
      previewHtml: {},
    })

    const handlePreviewContent = () => {
      const editor = editorRef.value

      drawer.previewHtml = editor.getHtml();
      drawer.visible = true;
    }

    const onDrawerClose =  () => {
      drawer.visible = false;
    }

    const model = reactive({
      expandedRowKeys:[] as number[],
      visible: false,
      loading: false,
      route: useRoute(),
    })

    const array2Tree = reactive({
      level1: [] as any[],
      SelectedData: [] as any[],
    })

    const postingDocs = reactive({
      doc: {},
      name : "",
      id: 0,
    })

    const setDisable = (treeSelectedData: any, id: any) => {
      // 遍历所有节点找到当前节点id
      for(let i = 0; i < treeSelectedData.length; i ++){
        const node = treeSelectedData[i];
        if(node.id === id) {
          // 找到之后设置为disable
          console.log("disabled", node);
          node.disabled = true;
          // 遍历找到节点的所有子节点，如果找到，设置为disable
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        }else {
          // 如果没有找到当前节点，则道其他子节点中寻找当前节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    }

    const ids: Array<string> = [];
    const name: Array<string> = [];


    const getDeletedItemsInfo = (treeSelectedData: any, id: any) => {
      // 遍历所有节点找到当前节点id
      for(let i = 0; i < treeSelectedData.length; i ++){
        const node = treeSelectedData[i];
        if(node.id === id) {
          // 找到之后设置为disable
          ids.push(id);
          name.push(node.name);

          // 遍历找到节点的所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeletedItemsInfo(children, children[j].id);
            }
          }
        }else {
          // 如果没有找到当前节点，则道其他子节点中寻找当前节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeletedItemsInfo(children, id);
          }
        }
      }
    }

    const handleQueryContent = () => {
      axios.get("doc/find-content/" + postingDocs.doc.id).then((response) => {
        const data = response.data;
        if(data.success){
          valueHtml.value = data.content;
        }else{
          message.error(data.message);
        }
      })
    }

    /*
    * edit
    * */
    const edit = (record: any) => {
      valueHtml.value = "";
      model.visible = true;
      postingDocs.doc = Tool.copy(record);
      handleQueryContent();

      array2Tree.SelectedData = Tool.copy(array2Tree.level1);
      setDisable(array2Tree.SelectedData, record.id);

      array2Tree.SelectedData.unshift({id:0, name:'无'});
    }
    /*
    * add
    * */
    const add = () => {

      valueHtml.value = "";
      model.visible = true;
      postingDocs.doc = {
        ebookId: model.route.query.ebookId,
      };
      // handleQueryContent();

      array2Tree.SelectedData = Tool.copy(array2Tree.level1);
      array2Tree.SelectedData.unshift({id:0, name:'无'});
    }


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

          array2Tree.SelectedData = Tool.copy(array2Tree.level1);
          array2Tree.SelectedData.unshift({id:0,name:'无'});

        }else{
          message.error(data.message);
        }
      })
    }




    const handleDelete = (id: number) => {
      ids.length = 0;
      name.length = 0;
      getDeletedItemsInfo(array2Tree.level1, id);
      axios.delete("doc/delete/" + ids.join(",")).then((response) => {
        const data = response.data
        if (data.success) {
          handleQuery();
        }
      });
    };


    const handleSave = () => {
      model.loading = true;
      postingDocs.doc.content = valueHtml;
      axios.post("doc/save", postingDocs.doc).then((response) => {
        model.loading = false;
        const data = response.data
        if (data.success) {
          message.success("Save Success");
          handleQuery();
        }else{
          message.error(data.message);
        }
      })
    }

    // const onExpand = (expanded: boolean, record: any) => {
    //   if (expanded) {
    //     if (!model.expandedRowKeys.includes(record.id)) {
    //       model.expandedRowKeys.push(record.id);
    //     }
    //   } else {
    //     model.expandedRowKeys = model.expandedRowKeys.filter(id => id !== record.id);
    //   }
    // };


    const showConfirm = (id:number) => {
      ids.length = 0;
      name.length = 0;
      getDeletedItemsInfo(array2Tree.level1, id);
      Modal.confirm({
        title: () => '删除确认',
        icon: () => createVNode(ExclamationCircleOutlined),
        content: () => `你将删除文档及其子文档（${name.join(',')}），确定删除吗？`,
        onOk() {
          handleDelete(id);
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        onCancel() {},
      });
    };

    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })

    const handleCreated = (editor:any) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }


    const columns = [
      { title: '名称', dataIndex: 'name', key: 'name' },
      {title: '操作', key: 'action',},
    ];


    onMounted(() => {
      handleQuery();
    });

    return {
      gettingDocs,
      postingDocs,
      // onExpand,
      array2Tree,
      columns,
      handleQuery,
      showConfirm,


      model,
      handleSave,
      edit,
      add,
      handleDelete,

      editorRef,
      valueHtml,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,

      drawer,
      handlePreviewContent,
      onDrawerClose,
    }
  }
});
</script>

