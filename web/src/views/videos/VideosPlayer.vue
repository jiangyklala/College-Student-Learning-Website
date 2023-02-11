<template>
  <!--  <p>{{$route.query.id}}</p>-->
  <a-layout>
    <a-layout-content class="layout-content">
      <span class="video-js-span">
        <video-player
            class="video-player"
            :options="videoOptions"
        >
        </video-player>
      </span>
    </a-layout-content>
    <a-layout-sider
        class="layout-sider"
        :style="{ overflow: 'auto', height: '50vh', position: 'fixed', right: 0}"
    >
      <a-menu v-model:selectedKeys="selectedKeys" theme="light" mode="vertical">
        <a-menu-item key="1">
          <span class="nav-text">nav 1</span>
        </a-menu-item>
        <a-menu-item key="2">
          <span class="nav-text">nav 2</span>
        </a-menu-item>
        <a-menu-item key="3">
          <span class="nav-text">nav 3</span>
        </a-menu-item>
        <a-menu-item key="4">
          <span class="nav-text">nav 4</span>
        </a-menu-item>
        <a-menu-item key="5">
          <span class="nav-text">nav 5</span>
        </a-menu-item>
        <a-menu-item key="6">
          <span class="nav-text">nav 6</span>
        </a-menu-item>
        <a-menu-item key="7">
          <span class="nav-text">nav 7</span>
        </a-menu-item>
        <a-menu-item key="8">
          <span class="nav-text">nav 8</span>
        </a-menu-item>
        <a-menu-item key="6">
          <span class="nav-text">nav 6</span>
        </a-menu-item>
        <a-menu-item key="7">
          <span class="nav-text">nav 7</span>
        </a-menu-item>
        <a-menu-item key="8">
          <span class="nav-text">nav 8</span>
        </a-menu-item>
        <a-menu-item key="6">
          <span class="nav-text">nav 6</span>
        </a-menu-item>
        <a-menu-item key="7">
          <span class="nav-text">nav 7</span>
        </a-menu-item>
        <a-menu-item key="8">
          <span class="nav-text">nav 8</span>
        </a-menu-item>
        <a-menu-item key="6">
          <span class="nav-text">nav 6</span>
        </a-menu-item>
        <a-menu-item key="7">
          <span class="nav-text">nav 7</span>
        </a-menu-item>
        <a-menu-item key="8">
          <span class="nav-text">nav 8</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
  </a-layout>

</template>

<script>
import {defineComponent, onMounted} from "vue";
import {VideoPlayer} from "vue-video-player";

export default defineComponent({
  components: {
    VideoPlayer,
  },
  name: 'VideosPlayer',
  setup() {

    const videoOptions = {
      playbackRates: [0.5, 1.0, 1.5, 1.75, 2.0], //播放速度
      autoplay: true,
      controls: true,
      fluid: true,
      preload: "auto", // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
      language: "zh-CN",
      aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
      notSupportedMessage: '此视频暂无法播放，请稍后再试', //允许覆盖Video.js无法播放媒体源时显示的默认信息。
      sources: [
        {
          src:
              'https://yiti-download-1309630359.cos.ap-shanghai.myqcloud.com/4097_1667126621.mp4',
          type: 'video/mp4'
        }
      ],
      controlBar: {
        timeDivider: true, // 当前时间和持续时间的分隔符
        durationDisplay: true, // 显示持续时间
        remainingTimeDisplay: true, // 是否显示剩余时间功能
        fullscreenToggle: true, // 是否显示全屏按钮
      },
    }


    onMounted(() => {

      console.log("ss")

    });

    return {
      videoOptions,
    };
  }
});
</script>

<style scoped>

.video-js-span {
}

.layout-content {
  padding: 20px 280px 30px 80px;
}

.layout-sider {

}

</style>