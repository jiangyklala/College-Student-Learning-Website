import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";
import VideoPlayer from '@videojs-player/vue';
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';
import VueMarkdownEditor from '@kangc/v-md-editor';
// 引入你所使用的主题 此处以 github 主题为例
import githubTheme from '@kangc/v-md-editor/lib/theme/github';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
// highlightjs
import hljs from 'highlight.js';

VueMarkdownEditor.use(githubTheme, {
    Hljs: hljs,
}).use(createCopyCodePlugin());


axios.defaults.baseURL = process.env.VUE_APP_SERVER;

const app = createApp(App);
app.use(store).use(router).use(Antd).use(VideoPlayer).use(mavonEditor).use(VueMarkdownEditor).mount('#app');


// 全局使用图标
const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}


if (process.env.NODE_ENV === "development") {
    /**
     * axios拦截器
     */
    axios.interceptors.request.use(function (config) {
        console.log('请求参数：', config);
        return config;
    }, error => {
        return Promise.reject(error);
    });
    axios.interceptors.response.use(function (response) {
        console.log('返回结果：', response);
        return response;
    }, error => {
        console.log('返回错误：', error);
        return Promise.reject(error);
    });

    console.log('环境:', process.env.NODE_ENV);
    console.log('服务端:', process.env.VUE_APP_SERVER);
    console.log('WS 服务端:', process.env.VUE_APP_WS_SERVER);
}


