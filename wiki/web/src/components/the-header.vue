<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">Home Page</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">User Manage Page</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook">Ebook Manage Page</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/category">
        <router-link to="/admin/category">Category Manage Page</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">About Us</router-link>
      </a-menu-item>

      <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
      >
        <a class="login-menu" v-show="loginConfig.user.id">
          <span>退出登录</span>
        </a>
      </a-popconfirm>
      <a class="login-menu" v-show="loginConfig.user.id">
        <span>您好：{{loginConfig.user.name}}</span>
      </a>
      <a class="login-menu" v-show="!loginConfig.user.id" @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>


    <a-modal
        title="登录"
        v-model:visible="loginConfig.visible"
        :confirm-loading="loginConfig.loading"
        @ok="login"
    >
      <a-form :model="loginConfig.user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginConfig.user.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginConfig.user.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>


  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent, computed, reactive} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',

  setup () {
    // 登录后保存
    const user = computed(() => store.state.user);

    // 用来登录
    
    const loginConfig = reactive({
      user:{
        loginName:"test",
        password:"test"
      },
      visible : false,
      loading : false,
    })


    const showLoginModal = () => {
      loginConfig.visible= true;
    };

    // 登录
    const login = () => {
      console.log("开始登录");
      loginConfig.loading = true;
      loginConfig.user.password = hexMd5(loginConfig.user.password + KEY);
      axios.post('/user/login', loginConfig.user).then((response) => {
        loginConfig.loading = false;
        const data = response.data;
        if (data.success) {
          loginConfig.visible = false;
          message.success("登录成功！");

          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // 退出登录
    const logout = () => {
      console.log("退出登录开始");
      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      showLoginModal,
      loginConfig,
      login,
      user,
      logout
    }
  }

});
</script>

<style>
.login-menu{
  float: right;
  color: white;
}
</style>