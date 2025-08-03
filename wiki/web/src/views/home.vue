<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          :style="{height:'100%', borderRight: 0}"
          mode="inline"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
            <MailOutlined />
            <span>Welcome</span>
        </a-menu-item>
        <a-sub-menu v-for="item in array2Tree.level1" :key="item.id">
          <template v-slot:title>
            <span>{{ item.name }}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="Home.showWelcome">
        <h1>欢迎使用</h1>
      </div>
      <a-list v-show="!Home.showWelcome" item-layout="vertical" size="large" :grid="{ gutter :20, column : 3}" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px" />
                {{ item.docCount }}
              </span>
              <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px" />
                {{ item.viewCount }}
              </span>
              <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px" />
                {{ item.voteCount }}
              </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{item.name}}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, toRef} from 'vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";
import {LikeOutlined} from "@ant-design/icons-vue";

const listData: any = [];

export default defineComponent({
  name: 'Home',
  methods: {LikeOutlined},
  setup() {

    const ebooksTmp = reactive(
        {books: []
        });

    const array2Tree = reactive({
      level1: [] as any[]
    });

    const Home = reactive({
      showWelcome: true,
      category2Id: 0,
    })

    let category: any[];


    const handleQueryCategory = () => {
      axios.get("category/all").then((response) => {
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


    const handleQueryEbook = () => {
      axios.get("/ebook/list",{
        params:{
          page:1,
          size:1000,
          category2Id: Home.category2Id,
        }
      }).then((response) => {
        const data = response.data;
        ebooksTmp.books = data.content.list;
      })
    }

    const handleClick = (value: any) =>{
      // console.log("click from menu");
      // Home.showWelcome = value.key === 'welcome';
      if(value.key === 'welcome'){
        Home.showWelcome = true;
      }else{
        Home.category2Id = value.key;
        Home.showWelcome = false;
        handleQueryEbook();
      }
    }




    // 一个生命周期函数，初始化写在onMounted里面
    onMounted(() => {
      handleQueryCategory();
      // handleQueryEbook();
    })

    return {

      Home,
      handleClick,
      array2Tree,
      handleQueryEbook,
      ebooks: toRef(ebooksTmp, "books"),
      listData,
      pagination: {
        pageSize: 3,
      },
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