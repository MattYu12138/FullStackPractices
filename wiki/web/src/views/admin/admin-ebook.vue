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
      <a-table :columns="columns"
               :data-source="ebooks">

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
              <a-button type="primary">编辑</a-button>
              <a-button type="default" danger>删除</a-button>
            </a-space>
          </template>
        </template>

      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, toRef, ref} from 'vue';
import axios from 'axios';

const listData: any = [];

export default defineComponent({
  name: 'Home',
  setup() {
    const ebooksTmp = reactive({books: []});
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);



    // 一个生命周期函数，初始化写在onMounted里面
    onMounted(() => {
      axios.get("/ebook/list").then((response) => {
        const data = response.data;
        ebooksTmp.books = data.content;
      })
    })




    // /**
    //  * 数据查询
    //  **/
    // const handleQuery = (params: any) => {
    //   loading.value = true;
    //   // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
    //   ebooks.value = [];
    //   axios.get("/ebook/list", {
    //     params: {
    //       page: params.page,
    //       size: params.size,
    //       name: param.value.name
    //     }
    //   }).then((response) => {
    //     loading.value = false;
    //     const data = response.data;
    //     if (data.success) {
    //       ebooks.value = data.content.list;
    //
    //       // 重置分页按钮
    //       pagination.value.current = params.page;
    //       pagination.value.total = data.content.total;
    //     } else {
    //       message.error(data.message);
    //     }
    //   });
    // };

    /**
     * 表格点击页码时触发
     */
    // const handleTableChange = (pagination: any) => {
    //   console.log("看看自带的分页参数都有啥：" + pagination);
    //   handleQuery({
    //     page: pagination.current,
    //     size: pagination.pageSize
    //   });
    // };

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        key: 'cover'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类1',
        dataIndex: 'category1Id',
        key: 'category1Id'
      },
          {
           title: '分类2',
            dataIndex: 'category2Id',
            key: ' category2Id'
          },
      {
        title: '文档数',
        dataIndex: 'docCount',
        key:'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',
        key:'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount',
        key:'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
      }
    ];

    return {
      ebooks: toRef(ebooksTmp, "books"),
      columns,
      listData,
      pagination,
      loading
      // handleQuery,
      // handleTableChange,
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

