<template>
  <a-layout-content style="padding: 0 50px">
    <div :style="{ background: '#fff', padding: '24px', minHeight: '280px' }">Content</div>
  </a-layout-content>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'Home',
  setup() {
    console.log("setup");
    axios.get("http://localhost:8880/downloadList/list?name=资料2").then(
        (response) => {
          console.log(response);
        }
    )
  }
});
</script>
