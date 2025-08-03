<template>
  <a-layout-footer style="text-align: center">
    Hello <span v-show="user.id">{{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted} from 'vue';
import store from "@/store";
import {Tool} from "@/util/tool";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user);

    let websocket : any;
    let token : any;

    const onOpen = () => {
      console.log('WebSocket successful connected，状态码：', websocket.readyState)
    };
    const onMessage = (event: any) => {
      console.log('WebSocket received：', event.data);
      notification['info']({
        message: 'message received',
        description: event.data,
      });
    };
    const onError = () => {
      console.log('WebSocket is fail to connect, state: ', websocket.readyState)
    };
    const onClose = () => {
      console.log('WebSocket closed，state: ', websocket.readyState)
    };
    const initWebSocket = () => {
      // 连接成功
      websocket.onopen = onOpen;
      // 收到消息的回调
      websocket.onmessage = onMessage;
      // 连接错误
      websocket.onerror = onError;
      // 连接关闭的回调
      websocket.onclose = onClose;
    };

    onMounted(() => {
      // WebSocket
      if ('WebSocket' in window) {
        token = Tool.uuid(10);
        // 连接地址：ws://127.0.0.1:8880/ws/xxx
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebSocket()

        // 关闭
        // websocket.close();
      } else {
        alert('browser not support')
      }
    });





    return {
      user
    }
  }
});


</script>