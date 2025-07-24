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
      <a-table
        :columns="columns"
        :data-source="ebooks.books"
        :pagination="ebooks.pagination"
        :loading="ebooks.loading"
        @change="handleTableChange"
    >

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img :src="record.cover" alt="cover" style="width: 60px" />
          </template>

          <!-- 分类 -->
<!--          <template v-else-if="column.key === 'category1d'">-->
<!--            <a-tag color="blue">{{ record.category1Id }}</a-tag>-->
<!--          </template>-->

<!--          <template v-else-if="column.key === 'category2d'">-->
<!--            <a-tag color="blue">{{ record.category2Id }}</a-tag>-->
<!--          </template>-->

<!--          <template v-else-if="column.key === 'docCount'">-->
<!--            <a-tag color="blue">{{ record.docCount }}</a-tag>-->
<!--          </template>-->

<!--          <template v-else-if="column.key === 'viewCount'">-->
<!--            <a-tag color="blue">{{ record.viewCount }}</a-tag>-->
<!--          </template>-->

          <!-- 操作 -->
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="primary" @click="edit(record)">编辑</a-button>
              <a-button type="default" danger>删除</a-button>
            </a-space>
          </template>
        </template>
        </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="电子书表单"
      v-model:visible="modal.visible"
      :confirm-loading="modal.loading"
      @ok="handleModalOk"
  >
    <a-form :model="ebooks.ebook" :label-col="{span :6 }" >
      <a-form-item label="封面">
        <a-input v-model:value="ebooks.ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebooks.ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebooks.ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebooks.ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebooks.ebook.description" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive} from 'vue';
import axios from 'axios';


export default defineComponent({
  name: 'Home',
  setup() {
    const ebooks = reactive({
      books: [],
      ebook: {},
      loading: false,
      pagination: {current: 1, pageSize: 3, total: 0}
    });

    const modal = reactive({
      text: "",
      visible: false,
      loading: false
    })

    const handleModalOk = () => {
      modal.text = 'The modal will be closed after two seconds';
      modal.loading = true;
      setTimeout(() => {
        modal.visible = false;
        modal.loading = false;
      }, 2000);
    }

    const edit = (record : any)=>{
      modal.visible = true;
      ebooks.ebook = record;
    }

    const handleQuery = (params:any) => {
      ebooks.loading = true;
      axios.get("ebook/list",
          {params: {
              page: params.page,
              size: params.size,
            }}).then((response)=>{
        ebooks.loading = false;
        const data = response.data;
          ebooks.books = data.content.list;

          ebooks.pagination.current = params.page;
          ebooks.pagination.pageSize = params.size;
          ebooks.pagination.total = data.content.total || 100;
      })
    }

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("分页参数是：", JSON.stringify(pagination, null, 2));
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    const columns = [
      {title: '封面', dataIndex: 'cover', key: 'cover'},
      {title: '名称', dataIndex: 'name'},
      {title: '分类1', dataIndex: 'category1Id', key: 'category1Id'},
      {title: '分类2', dataIndex: 'category2Id', key: ' category2Id'},
      {title: '文档数', dataIndex: 'docCount', key:'docCount'},
      {title: '阅读数', dataIndex: 'viewCount', key:'viewCount'},
      {title: '点赞数', dataIndex: 'voteCount', key:'voteCount'},
      {title: 'Action', key: 'action',}
    ];

    onMounted(() => {
      handleQuery({
        page: ebooks.pagination.current,  // 当前页
        size: ebooks.pagination.pageSize, // 每页条数（默认配置的值）
      });
    });

    return {
      ebooks,
      columns,
      handleTableChange,


      modal,
      handleModalOk,
      edit,
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

