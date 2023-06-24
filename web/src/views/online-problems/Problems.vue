<template>
  <a-layout-content class="layout-content">
    <div>
      <h2>{{ question.title }}</h2>
      <a-radio-group v-model:value="selectedOption" name="radioGroup">ra
        <a-radio v-for="option in question.options" :key="option.id" :value="option.id">{{ option.label }}</a-radio>
      </a-radio-group>
      <a-button type="primary" @click="submitAnswer">提交答案</a-button>
      <div v-if="showResult">
        <p v-if="selectedOption === question.correctOption">回答正确！</p>
        <p v-else>回答错误！正确答案是：{{ question.correctOption }}</p>
      </div>
    </div>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";

export default defineComponent({
  name: "Problems",
  setup() {

    const question = {
      title: '这是一个问题的标题',
      options: [
        {id: 1, label: '选项1'},
        {id: 2, label: '选项2'},
        {id: 3, label: '选项3'},
        {id: 4, label: '选项4'}
      ],
      correctOption: 2
    };

    const selectedOption = ref<number>(0);

    const showResult = ref(false);

    const submitAnswer = () => {
      showResult.value = true;
    }

    return {
      question,
      selectedOption,
      showResult,

      submitAnswer,
    }
  }
})
</script>

<style scoped>
.layout-content {
  padding-top: 60px;
  width: 80%;
  height: 100%;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(237, 239, 242);
}
</style>