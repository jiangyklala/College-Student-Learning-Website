<template>
  <div class="container">
    <smile-two-tone style="font-size: 35px; padding-top: 5px"/>

    <div class="content">
      <div class="text">
        {{content}}
      </div>
    </div>

  </div>
</template>

<script>

import {defineComponent} from "vue";
import {FireTwoTone, SmileTwoTone} from '@ant-design/icons-vue';


export default defineComponent({
  name: 'right-chat-item',
  props: ['content'],
  components: {
    FireTwoTone,
    SmileTwoTone,
  },
  setup : function () {

  return {

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