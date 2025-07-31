<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-form layout="inline" :model="postingUsers" style="margin-bottom: 2vw">
        <a-form-item>
          <a-input v-model:value="postingUsers.loginName" aria-placeholder="登陆名">
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery({page:1, size: gettingUsers.pagination.pageSize})">
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
          :data-source="gettingUsers.user"
          :pagination="gettingUsers.pagination"
          :loading="gettingUsers.loading"
          @change="handleTableChange"
      >

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img :src="record.cover" alt="cover" style="width: 60px"/>
          </template>
          <!-- 操作 -->
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="resetPassword(record)">
                重置密码
              </a-button>
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
      title="用户表单"
      v-model:open="model.visible"
      :confirm-loading="model.loading"
      @ok="handleModelOk"
  >
    <a-form :model="postingUsers.user" :label-col="{span :6 }" :wrapper-col="{span :18 }">
      <a-form-item label="登录名">
        <a-input v-model:value="postingUsers.user.loginName" :disabled="!!postingUsers.user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="postingUsers.user.name"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool'
import {useRoute} from "vue-router";


export default defineComponent({
  name: 'AdminUser',
  setup() {

    interface User {
      id?: number;
      loginName?: string;
      name?: string;
      password?: string;
    }


    const gettingUsers = reactive({
      user: [],
      loading: false,
      pagination: {current: 1, pageSize: 5, total: 0}
    });


    const model = reactive({
      visible: false,
      loading: false,
    })

    const postingUsers = reactive<{
      user: User;
      loginName: string;
      id: number;
    }>({
      user: {},
      loginName: "",
      id: 0,
    })

    /*
    * edit
    * */
    const edit = (record: any) => {
      model.visible = true;
      postingUsers.user = Tool.copy(record);
    };


    /*
    * add
    * */
    const add = () => {
      model.visible = true;
      postingUsers.user = {};
    }

    const handleQuery = (params: any) => {
      gettingUsers.loading = true;
      //防止更改后，在表单还是显示原来数值
      gettingUsers.user = [];
      axios.get("user/list",
          {
            params: {
              page: params.page,
              size: params.size,
              loginName: postingUsers.loginName,
            }
          }).then((response) => {
        gettingUsers.loading = false;
        const data = response.data;
        if (data.success) {
          gettingUsers.user = data.content.list;

          gettingUsers.pagination.current = params.page;
          gettingUsers.pagination.total = data.content.total;
        } else {
          message.error(data.message);
        }

      })
    }




    const handleDelete = (id: number) => {
      axios.delete("user/delete/" + id).then((response) => {
        const data = response.data
        if (data.success) {
          handleQuery({
            page: gettingUsers.pagination.current,  // 当前页
            size: gettingUsers.pagination.pageSize, // 每页条数（默认配置的值）
          });
        }
      });
    };

    // 点击保存
    const handleModelOk = () => {
      model.loading = true;

      postingUsers.user.password = hexMd5(postingUsers.user.password + KEY);

      axios.post("user/save", postingUsers.user).then((response) => {
        model.loading = false;
        const data = response.data
        if (data.success) {
          model.visible = false;

          handleQuery({
            page: gettingUsers.pagination.current,  // 当前页
            size: gettingUsers.pagination.pageSize, // 每页条数（默认配置的值）
          });
        } else {
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
      {title: '登陆名', dataIndex: 'loginName'},
      {title: '名称', dataIndex: 'name'},
      {title: '密码', dataIndex: 'password'},
      {title: 'Action', key: 'action',}
    ];

    onMounted(() => {
      handleQuery({
        page:1,
        size: gettingUsers.pagination.pageSize
      });
    });

    return {
      gettingUsers,
      columns,
      postingUsers,
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

