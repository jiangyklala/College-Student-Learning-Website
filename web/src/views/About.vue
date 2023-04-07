<template>
  <div class="about">
    <h1>This is an about page</h1>
    <button @click="lala">asfas</button>
  </div>
</template>


<script lang="ts">
import {defineComponent} from 'vue';
import router from "@/router";

export default defineComponent({
  name: 'About',
  setup () {

    const lala = () => {
      router.push({
        path: 'Home',
      })
    }

    return {
      lala,
    }
  }
});
</script>

<style>

</style>
