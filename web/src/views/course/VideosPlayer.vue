<template>
  <a-layout>
    <a-layout-content class="layout-content">
      <span class="mainSpan">
        <div class="video-js-div">
          <video-player
              v-if="isLive"
              class="video-player vjs-big-play-centered"
              :options="videoOptions"
          >
          </video-player>
        </div>
        <div class="category-div">
          <a-table
              class="table-category"
              :columns="columns"
              :data-source="data"
              :scroll="{ x: 0 , y: 200 }"
              :pagination="false"
          ></a-table>
        </div>
        <div class="courseInfo-div">
          <div class="courseName-div">
            {{ courseItemInfo.name }}
          </div>
          <a-divider style="height: 18px; font-size: 25px">课程概述</a-divider>
          <div class="courseDes-div">
            {{ courseItemInfo.description }}
          </div>
        </div>
      </span>
    </a-layout-content>
  </a-layout>

</template>

<script lang="ts">
import {defineComponent, nextTick, onMounted, ref} from "vue";
import {VideoPlayer} from "vue-video-player";
import axios from "axios";
import {message} from "ant-design-vue";
import "video.js/dist/video-js.css";


export default defineComponent({
  components: {
    VideoPlayer,
  },
  name: 'VideosPlayer',
  setup() {
    let itemCourse: any;
    const courseItemInfo = ref("");
    const courseItem = ref();
    const isLive = ref(false);

    const columns = [
      {title: '课程目录', width: 100, dataIndex: 'name', key: 'name', fixed: 'left'},
      {title: '', width: 0},
    ];

    const data = [
      {
        key: '1',
        name: 'John Brown',
      },
      {
        key: '2',
        name: 'Jim Green',
      },
      {
        key: '2',
        name: 'Jim Green',
      },
      {
        key: '2',
        name: 'Jim Green',
      },
      {
        key: '2',
        name: 'Jim Green',
      },
      {
        key: '2',
        name: 'Jim Green',
      }
    ];

    /**
     * 视频播放配置
     */
    const videoOptions = {
      playbackRates: [0.5, 1.0, 1.5, 1.75, 2.0],        //播放速度
      autoplay: false,
      controls: true,
      fluid: true,
      preload: "auto",                                  // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
      language: "zh-CN",
      aspectRatio: '16:9',                              // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
      notSupportedMessage: '此视频暂无法播放，请稍后再试',   // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
      sources: [
        {
          src: '',
          type: 'video/mp4'
        }
      ],
      controlBar: {
        timeDivider: true,                            // 当前时间和持续时间的分隔符
        durationDisplay: true,                        // 显示持续时间
        remainingTimeDisplay: true,                   // 是否显示剩余时间功能
        fullscreenToggle: true,                       // 是否显示全屏按钮
      },

    };

    const getCourseItemByCourse = (itemCourse: any) => {
      axios.get("/courseItem/select", {
        params: {
          page: 1,
          size: 100,          // 全查
          course: itemCourse,
        }
      }).then((response) => {

        if (response.data.success) {
          courseItem.value = response.data.content.list;                 // 获取当前课程目录下的所有视频

          videoOptions.sources[0].src = courseItem.value[0].videoLink;   // 播放其中的第一个视频
          isLive.value = false;
          nextTick(() => {
            isLive.value = true;
          })


        } else {
          message.error(response.data.message);
        }
      })
    }

    /**
     * 页面初始化
     */
    const initData = () => {
      let courseItemInfoJSON: any;
      itemCourse = sessionStorage.getItem("CourseItem");
      courseItemInfoJSON = sessionStorage.getItem("CourseItemInfo");
      if (courseItemInfoJSON != null) {
        courseItemInfo.value = JSON.parse(courseItemInfoJSON);
        // console.log(courseItemInfo.value);
      }
      getCourseItemByCourse(itemCourse);
    }

    onMounted(() => {
      initData();
    });

    return {
      courseItem,
      videoOptions,
      data,
      columns,
      courseItemInfo,
      isLive,
    };
  },
});
</script>

<style scoped>
.layout-content {
  padding: 20px 20px 30px 80px;
  width: 1200px;
  height: 800px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: #fff;
}

.vjs-big-play-button {
  font-size: 3em;
  line-height: 1.5em;
  height: 1.63332em;
  width: 3em;
  display: block;
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 0;
  cursor: pointer;
  opacity: 1;
  border: 0.06666em solid #fff;
  background-color: #2B333F;
  background-color: rgba(43, 51, 63, 0.7);
  border-radius: 0.3em;
  transition: all 0.4s;
}

.mainSpan {
  position: absolute;
}

.video-js-div {
  float: left;
}

.video-player {
  width: 700px !important;
}

.category-div {
  float: right;
}

.table-category {
  margin-left: 50px;
  width: 350px !important;
}

.courseInfo-div {
  padding-top: 400px;
  width: 700px;
  font-size: 20px;
}

.courseDes-div {
  padding-top: 10px;
}

.courseName-div {
  padding-top: 10px;
}

</style>