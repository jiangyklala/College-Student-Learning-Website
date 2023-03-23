<template>
  <a-layout class="layout">
    <the-header></the-header>
      <router-view/>
    <the-footer></the-footer>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import theHeader from "@/components/the-header.vue";
import theFooter from "@/components/the-footer.vue";
import axios from "axios";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'App',
  components: {
    theHeader,
    theFooter,
  },
  setup() {




    return {

    };
  }
});
</script>

<style>
.site-layout-content {
  min-height: 280px;
  padding: 24px;
  background: #fff;
}

#components-layout-demo-top .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

[data-theme='dark'] .site-layout-content {
  background: #141414;
}


</style>