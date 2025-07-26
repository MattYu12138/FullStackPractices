<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-form layout="inline" :model="postingEbooks" style="margin-bottom: 2vw">
        <a-form-item>
          <a-input v-model:value="postingEbooks.name" aria-placeholder="名称">
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery({page:1, size: gettingEbooks.pagination.pageSize})">
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
          :data-source="gettingEbooks.ebook"
          :pagination="gettingEbooks.pagination"
          :loading="gettingEbooks.loading"
          @change="handleTableChange"
      >

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img :src="record.cover" alt="cover" style="width: 60px"/>
          </template>
          <template v-else-if="column.key === 'category'">
            <span>{{getCategoryName(record.category1Id)}} / {{getCategoryName(record.category2Id)}}</span>
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
    <a-form :model="postingEbooks.ebook" :label-col="{span :6 }">
      <a-form-item label="封面">
        <a-input v-model:value="postingEbooks.ebook.cover"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="postingEbooks.ebook.name"/>
      </a-form-item>
<!--      <a-form-item label="分类一">-->
<!--        <a-input v-model:value="postingEbooks.ebook.category1Id"/>-->
<!--      </a-form-item>-->
<!--      <a-form-item label="分类二">-->
<!--        <a-input v-model:value="postingEbooks.ebook.category2Id"/>-->
<!--      </a-form-item>-->
      <a-form-item label="分类">
        <a-cascader
            v-model:value="Categorys.ids"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="array2Tree.level1"/>
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="postingEbooks.ebook.description"/>
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

    interface Ebook {
      id?: number;
      name?: string;
      cover?: string;
      description?: string;
      category1Id?: number;
      category2Id?: number;
    }

    let category: any[];

    const gettingEbooks = reactive({
      ebook: [],
      loading: false,
      pagination: {current: 1, pageSize: 5, total: 0}
    });

    const Categorys = reactive<{
      ids: number[];
    }>({
      ids: [],
    });

    const array2Tree = reactive({
      level1: [] as any[]
    })

    const model = reactive({
      visible: false,
      loading: false
    })

    const postingEbooks = reactive<{
      ebook: Ebook;
      name: string;
      id: number;
    }>({
      ebook: {},
      name : "",
      id: 0,
    })

    /*
    * edit
    * */
    const edit = (record: any) => {
      model.visible = true;
      postingEbooks.ebook = Tool.copy(record);
      Categorys.ids = [postingEbooks.ebook.category1Id ?? 0, postingEbooks.ebook.category2Id ?? 0]
    };


    /*
    * add
    * */
    const add = () => {
      model.visible = true;
      postingEbooks.ebook = {};
    }

    const handleQuery = (params: any) => {
      gettingEbooks.loading = true;
      //防止更改后，在表单还是显示原来数值
      gettingEbooks.ebook = [];
      axios.get("ebook/list",
          {
            params: {
              page: params.page,
              size: params.size,
              name: postingEbooks.name,
            }
          }).then((response) => {
        gettingEbooks.loading = false;
        const data = response.data;
        if(data.success){
          gettingEbooks.ebook = data.content.list;

          gettingEbooks.pagination.current = params.page;
          gettingEbooks.pagination.pageSize = params.size;
          gettingEbooks.pagination.total = data.content.total;
        }else{
          message.error(data.message);
        }

      })
    }



    const handleQueryCategory = () => {
      model.loading = true;
      axios.get("category/all").then((response) => {
        model.loading = false;
        const data = response.data;
        if(data.success){
          category = data.content;
          console.log("original: " , category);

          array2Tree.level1 = [];
          array2Tree.level1 = Tool.array2Tree(category, 0);
          console.log("树形结构: " , array2Tree.level1);

        }else{
          message.error(data.message);
        }

      })
    }


    const handleDelete = (id: number) => {
      axios.delete("ebook/delete/" + id).then((response) => {
        const data = response.data
        if (data.success) {
          handleQuery({
            page: gettingEbooks.pagination.current,  // 当前页
            size: gettingEbooks.pagination.pageSize, // 每页条数（默认配置的值）
          });
        }
      });
    };

    const getCategoryName = (cid: number) =>{
      let result = "";
      category.forEach((item: any) =>{
        if(item.id === cid){
          result = item.name;
        }
      });
      return result;
    }

    // 点击保存
    const handleModelOk = () => {
      model.loading = true;
      postingEbooks.ebook.category1Id = Categorys.ids[0];
      postingEbooks.ebook.category2Id = Categorys.ids[1];
      axios.post("ebook/save", postingEbooks.ebook).then((response) => {
        model.loading = false;
        const data = response.data
        if (data.success) {
          model.visible = false;

          handleQuery({
            page: gettingEbooks.pagination.current,  // 当前页
            size: gettingEbooks.pagination.pageSize, // 每页条数（默认配置的值）
          });
        }else{
          message.error(data.message);
        }
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
      {title: '分类', dataIndex:'category', key: 'category'},
      {title: '描述', dataIndex:'description', key:'description'},
      {title: '文档数', dataIndex: 'docCount', key: 'docCount'},
      {title: '阅读数', dataIndex: 'viewCount', key: 'viewCount'},
      {title: '点赞数', dataIndex: 'voteCount', key: 'voteCount'},
      {title: 'Action', key: 'action',}
    ];

    onMounted(() => {
      handleQueryCategory();
      handleQuery({
        page: gettingEbooks.pagination.current,  // 当前页
        size: gettingEbooks.pagination.pageSize, // 每页条数（默认配置的值）
      });
    });

    return {
      gettingEbooks,
      columns,
      postingEbooks,
      Categorys,
      array2Tree,
      getCategoryName,
      handleQuery,

      handleTableChange,


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

