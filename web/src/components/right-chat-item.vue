<template>
  <div class="container">
    <smile-two-tone style="font-size: 35px; padding-top: 5px"/>

    <div class="content">
      <div
          @click="copyToClipboard"
          class="text">
        {{content}}
      </div>
    </div>

  </div>
</template>

<script lang="ts">

import {defineComponent} from "vue";
import {FireTwoTone, SmileTwoTone} from '@ant-design/icons-vue';
import {message} from "ant-design-vue";


export default defineComponent({
  name: 'right-chat-item',
  props: ['content'],
  components: {
    FireTwoTone,
    SmileTwoTone,
  },
  setup : function (props : any) {

    const copyToClipboard = () => {
      navigator.clipboard.writeText(props.content).then(() => {
        message.success('内容已复制到剪贴板');
      }, () => {
        console.error('复制失败');
      });
    }

  return {
    copyToClipboard,
  }

}
});

</script>

<style scoped>
.container {
  display: flex;
  padding: 10px 15px;
  margin-left: 60px;
  flex-direction: row-reverse;
}

.head {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 1px solid #eee;
}

.content {
  margin-right: 10px;
  margin-top: 10px;
}

.text {
  background-color: #eee;
  border-bottom-left-radius: 10px;
  padding: 5px 5px;
  font-size: 14px;
  color: #000;
}

.img {
  width: 100px;
  border-bottom-right-radius: 10px;
  border-top-right-radius: 10px;
  border-bottom-left-radius: 10px;
}



</style>