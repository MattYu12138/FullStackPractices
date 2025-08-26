<template>
  <div>
    <div v-if="!open" class="chat-entry" @click="open = true"></div>
    <div v-else class="chat-window">
      <div class="chat-header">
        <span>AI助理</span>
        <span class="close" @click="open = false">×</span>
      </div>
      <div class="chat-body">
        <div v-for="(m, index) in messages" :key="index" class="msg" :class="m.role">
          {{ m.content }}
        </div>
      </div>
      <div class="chat-input">
        <input v-model="text" @keyup.enter="send" placeholder="请输入内容..." />
        <button @click="send">发送</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
}

export default defineComponent({
  name: 'the-chat',
  setup() {
    const open = ref(false);
    const text = ref('');
    const messages = ref<ChatMessage[]>([]);

    const send = () => {
      if (!text.value) return;
      messages.value.push({ role: 'user', content: text.value });

      const reply = '你好，这是一个流式回复示例。';
      const chars = reply.split('');
      const idx = messages.value.push({ role: 'assistant', content: '' }) - 1;
      let i = 0;
      const timer = setInterval(() => {
        messages.value[idx].content += chars[i++];
        if (i >= chars.length) {
          clearInterval(timer);
        }
      }, 50);

      text.value = '';
    };

    return {
      open,
      text,
      messages,
      send,
    };
  },
});
</script>

<style scoped>
.chat-entry {
  position: fixed;
  right: 24px;
  bottom: 24px;
  width: 48px;
  height: 48px;
  background: #1890ff;
  border-radius: 50%;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}
.chat-window {
  position: fixed;
  right: 24px;
  bottom: 24px;
  width: 320px;
  height: 400px;
  background: #fff;
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
.chat-header {
  display: flex;
  justify-content: space-between;
  padding: 8px;
  background: #1890ff;
  color: #fff;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}
.chat-body {
  flex: 1;
  padding: 8px;
  overflow-y: auto;
}
.msg {
  margin-bottom: 8px;
}
.msg.user {
  text-align: right;
  color: #1890ff;
}
.msg.assistant {
  text-align: left;
}
.chat-input {
  display: flex;
  border-top: 1px solid #f0f0f0;
}
.chat-input input {
  flex: 1;
  border: none;
  padding: 8px;
  outline: none;
}
.chat-input button {
  width: 60px;
  border: none;
  background: #1890ff;
  color: #fff;
  cursor: pointer;
}
.close {
  cursor: pointer;
}
</style>
