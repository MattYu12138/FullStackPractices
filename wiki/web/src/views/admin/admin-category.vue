<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:openKeys="openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <template #title>
              <span>
                <user-outlined/>
                subnav 11111111
              </span>
          </template>
          <a-menu-item key="1">option1</a-menu-item>
          <a-menu-item key="2">option2</a-menu-item>
          <a-menu-item key="3">option3</a-menu-item>
          <a-menu-item key="4">option4</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <template #title>
              <span>
                <laptop-outlined/>
                subnav 2
              </span>
          </template>
          <a-menu-item key="5">option5</a-menu-item>
          <a-menu-item key="6">option6</a-menu-item>
          <a-menu-item key="7">option7</a-menu-item>
          <a-menu-item key="8">option8</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub3">
          <template #title>
              <span>
                <notification-outlined/>
                subnav 3
              </span>
          </template>
          <a-menu-item key="9">option9</a-menu-item>
          <a-menu-item key="10">option10</a-menu-item>
          <a-menu-item key="11">option11</a-menu-item>
          <a-menu-item key="12">option12</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-form layout="inline" :model="postingCategorys" style="margin-bottom: 2vw">
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
          :loading="gettingCategorys.loading"
          :pagination="false"
          :row-key="record => record.id"
          :expandedRowKeys="array2Tree.expandedRowKeys"
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
      title="电子书表单"
      v-model:open="model.visible"
      :confirm-loading="model.loading"
      @ok="handleModelOk"
  >
    <a-form :model="postingCategorys.category" :label-col="{ span: 6 }">
      <a-form-item label="名称">
        <a-input v-model:value="postingCategorys.category.name" />
      </a-form-item>

      <a-form-item label="父ID">
        <a-input-number v-model:value="postingCategorys.category.parent" :min="0" style="width: 100%" />
      </a-form-item>

      <a-form-item label="顺序">
        <a-input-number v-model:value="postingCategorys.category.sort" :min="0" style="width: 100%" />
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


    const gettingCategorys = reactive({
      category: [],
      loading: false,
      pagination: {current: 1, pageSize: 5, total: 0}
    });
    const model = reactive({
      expandedRowKeys:[] as number[],
      visible: false,
      loading: false
    })

    const array2Tree = reactive({
      level1: [] as any[],

    })

    const postingCategorys = reactive({
      category: {},
      name : "",
      id: 0,
    })

    /*
    * edit
    * */
    const edit = (record: any) => {
      model.visible = true;
      postingCategorys.category = Tool.copy(record);
    }
    /*
    * add
    * */
    const add = () => {
      model.visible = true;
      postingCategorys.category = {};
    }



    const handleQuery = () => {
      gettingCategorys.loading = true;
      axios.get("category/all").then((response) => {
        gettingCategorys.loading = false;
        const data = response.data;
        if(data.success){
          gettingCategorys.category = data.content;
          console.log("original: " , gettingCategorys.category);

          array2Tree.level1 = [];
          array2Tree.level1 = Tool.array2Tree(gettingCategorys.category, 0);
          console.log("树形结构: " , array2Tree.level1);

        }else{
          message.error(data.message);
        }

      })
    }



    const handleDelete = (id: number) => {
      axios.delete("category/delete/" + id).then((response) => {
        const data = response.data
        if (data.success) {
          handleQuery();
        }
      });
    };


    const handleModelOk = () => {
      model.loading = true;
      axios.post("category/save", postingCategorys.category).then((response) => {
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
      gettingCategorys,
      postingCategorys,
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

