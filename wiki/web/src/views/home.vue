<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          :style="{height:'100%', borderRight: 0}"
          mode="inline"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <router-link to="/">
            <MailOutlined />
            <span>Welcome</span>
          </router-link>
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
      <a-list item-layout="vertical" size="large" :grid="{ gutter :20, column : 3}" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ icon, text } in actions" :key="icon">
            <component :is="icon" style="margin-right: 8px"/>
            {{ text }}
          </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.title }}</a>
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

const listData: any = [];

export default defineComponent({
  name: 'Home',
  setup() {

    const ebooksTmp = reactive(
        {books: []
        });

    const array2Tree = reactive({
      level1: [] as any[]
    });

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

    const handleClick = () =>{
      console.log("click from menu");
    }

    // 一个生命周期函数，初始化写在onMounted里面
    onMounted(() => {
      handleQueryCategory();
      axios.get("/ebook/list",{
        params:{
          page:1,
          size:1000
        }
      }).then((response) => {
        const data = response.data;
        ebooksTmp.books = data.content.list;
      })
    })

    return {
      handleClick,
      array2Tree,
      ebooks: toRef(ebooksTmp, "books"),
      listData,
      pagination: {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions: [
        {
          icon: 'StarOutlined',
          text: '15622',
        },
        {
          icon: 'LikeOutlined',
          text: '15611',
        },
        {
          icon: 'MessageOutlined',
          text: '2',
        },
      ],
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