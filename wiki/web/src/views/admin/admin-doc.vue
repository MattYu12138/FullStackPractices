<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
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
          :columns="columns"
          :data-source="array2Tree.level1"
          :loading="model.loading"
          :pagination="false"
          :row-key="record => record.id"
          :expandedRowKeys="model.expandedRowKeys"
          @expand="onExpand"
      >

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img :src="record.cover" alt="cover" style="width: 60px"/>
          </template>

          <!-- 操作 -->
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="primary" @click="edit(record)">编辑</a-button>
              <a-popconfirm
                  title="Are you sure delete this task?"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="default">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="文档表单"
      v-model:open="model.visible"
      :confirm-loading="model.loading"
      @ok="handleModelOk"
  >
    <a-form :model="postingDocs.doc" :label-col="{ span: 6 }">
      <a-form-item label="名称">
        <a-input v-model:value="postingDocs.doc.name" />
      </a-form-item>

      <a-form-item label="parent doc">
        <a-tree-select
            v-model:value="postingDocs.doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="array2Tree.SelectedData"
            placeholder="请选择父文档"
            tree-default-expand-all
            :field-names="{ label: 'name', key: 'id', value: 'id' }"
        >
        </a-tree-select>
      </a-form-item>

      <a-form-item label="顺序">
        <a-input-number v-model:value="postingDocs.doc.sort" :min="0" style="width: 100%" />
      </a-form-item>
    </a-form>

  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool'


export default defineComponent({
  name: 'Home',
  setup() {


    const gettingDocs = reactive({
      doc: [],
      pagination: {current: 1, pageSize: 5, total: 0}
    });

    const model = reactive({
      expandedRowKeys:[] as number[],
      visible: false,
      loading: false
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

    /*
    * edit
    * */
    const edit = (record: any) => {
      model.visible = true;
      postingDocs.doc = Tool.copy(record);

      array2Tree.SelectedData = Tool.copy(array2Tree.level1);
      setDisable(array2Tree.SelectedData, record.id);

      array2Tree.SelectedData.unshift({id:0, name:'无'});
    }
    /*
    * add
    * */
    const add = () => {
      model.visible = true;
      postingDocs.doc = {};

      array2Tree.SelectedData = Tool.copy(array2Tree.level1);
      array2Tree.SelectedData.unshift({id:0, name:'无'});
    }



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


    const handleDelete = (id: number) => {
      axios.delete("doc/delete/" + id).then((response) => {
        const data = response.data
        if (data.success) {
          handleQuery();
        }
      });
    };


    const handleModelOk = () => {
      model.loading = true;
      axios.post("doc/save", postingDocs.doc).then((response) => {
        model.loading = false;
        const data = response.data
        if (data.success) {
          model.visible = false;

          handleQuery();
        }else{
          message.error(data.message);
        }
      })
    }

    const onExpand = (expanded: boolean, record: any) => {
      if (expanded) {
        if (!model.expandedRowKeys.includes(record.id)) {
          model.expandedRowKeys.push(record.id);
        }
      } else {
        model.expandedRowKeys = model.expandedRowKeys.filter(id => id !== record.id);
      }
    };


    const columns = [
      { title: '名称', dataIndex: 'name', key: 'name' },
      { title: '父ID', dataIndex: 'parent', key: 'parent' },
      { title: '顺序', dataIndex: 'sort', key: 'sort' },
      {title: '操作', key: 'action',},
    ];

    onMounted(() => {
      handleQuery();
    });

    return {
      gettingDocs,
      postingDocs,
      onExpand,
      array2Tree,
      columns,
      handleQuery,


      model,
      handleModelOk,
      edit,
      add,
      handleDelete,
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

