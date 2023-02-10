<template>
  <!--  <p>{{$route.query.id}}</p>-->
  <div>
    <video-player
        class="video-js"
        :options="videoOptions"
        controls
        :width="1000"
    >
    </video-player>
  </div>
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
      autoplay: true,
      controls: true,
      fluid: true,
      preload: "auto", // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
      language: "zh-CN",
      aspectRatio: "16:9",
      sources: [
        {
          src:
              'https://yiti-download-1309630359.cos.ap-shanghai.myqcloud.com/4097_1667126621.mp4',
          type: 'video/mp4'
        }
      ],
      controlBar: {
        volumeControl: true,
        timeDivider: true, // 当前时间和持续时间的分隔符
        durationDisplay: true, // 显示持续时间
        remainingTimeDisplay: false, // 是否显示剩余时间功能
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

</style>