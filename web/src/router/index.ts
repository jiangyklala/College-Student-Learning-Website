import {createRouter, createWebHistory, NavigationGuardNext, RouteLocationNormalized, RouteRecordRaw} from 'vue-router'
import Home from "@/views/Home.vue";
import Chatgpt from "@/views/chatgpt/Chatgpt.vue";
import Image from "@/views/chatgpt/Image.vue";
import OnlineTable from "@/views/others/online-table/OnlineTable.vue";

const routes: Array<RouteRecordRaw> = [
    {
        path: '/home',
        name: 'home',
        component: Home
    },
    {
        path: '/about',
        name: 'about',
        // 懒加载的写法
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    },
    {
        path: '/download/Download',
        name: 'download',
        component: () => import('../views/download/Download.vue')
    },
    {
        path: '/course/VideosPlayer',
        name: 'VideosPlayer',
        component: () => import('../views/course/VideosPlayer.vue')

    },
    {
        path: '/admin/AdminDownload',
        name: 'AdminDownload',
        component: () => import('../views/admin/AdminDownload.vue')

    },
    {
        path: '/admin/AdminQuestion',
        name: 'AdminQuestion',
        component: () => import('../views/admin/AdminQuestion.vue')

    },
    {
        path: '/admin/AdminCategory',
        name: 'AdminCategory',
        component: () => import('../views/admin/AdminCategory.vue')

    },
    {
        path: '/admin/AdminWxSpecialPage',
        name: 'AdminWxSpecialPage',
        component: () => import('../views/admin/AdminWxSpecialPage.vue')

    },
    {
        path: '/course/Course',
        name: 'Course',
        component: () => import('../views/course/Course.vue')

    },
    {
        path: '/admin/AdminCourse',
        name: 'AdminCourse',
        component: () => import('../views/admin/AdminCourse.vue')

    },
    {
        path: '/admin/AdminWxQuestion',
        name: 'AdminWxQuestion',
        component: () => import('../views/admin/AdminWxQuestion.vue')

    },
    {
        path: '/column/Column',
        name: 'Column',
        component: () => import('../views/column/Column.vue')

    },
    {
        path: '/admin/AdminColumn',
        name: 'AdminColumn',
        component: () => import('../views/admin/AdminColumn.vue')

    },
    {
        path: '/admin/AdminDoc',
        name: 'AdminDoc',
        component: () => import('../views/admin/AdminDoc.vue')

    },
    {
        path: '/admin/AdminUsers',
        name: 'AdminUsers',
        component: () => import('../views/admin/AdminUsers.vue')

    },
    {
        path: '/admin/AdminConfig',
        name: 'AdminConfig',
        component: () => import('../views/admin/AdminConfig.vue')

    },
    {
        path: '/admin/Admin',
        name: 'Admin',
        component: () => import('../views/admin/Admin.vue')
    },
    {
        path: '/column/Doc',
        name: 'Doc',
        component: () => import('../views/column/Doc.vue')

    },
    {
        path: '/chatgpt/Chatgpt',
        name: 'Chatgpt',
        component: Chatgpt
    },
    {
        path: '/',
        name: '/',
        component: Chatgpt
    },
    {
        path: '/chatgpt/Image',
        name: 'Image',
        component: Image
    },
    {
        path: '/chatgpt/Pay',
        name: 'Pay',
        component: () => import('../views/chatgpt/Pay.vue')
    },
    {
        path: '/chatgpt/Invite',
        name: 'Invite',
        component: () => import('../views/chatgpt/Invite.vue')
    },
    {
        path: '/chatgpt/Paying/:chooseValue',
        name: 'Paying',
        component: () => import('../views/chatgpt/Paying.vue')
    },
    {
        path: '/online-problems/Problems',
        name: 'Problems',
        component: () => import('../views/online-problems/Problems.vue')
    },
    {
        path: '/online-problems/DoExam',
        name: 'DoExam',
        component: () => import('../views/online-problems/DoExam.vue')

    },
    {
        path: '/others/online-table',
        name: 'OnlineTable',
        component: OnlineTable,
        meta: {exclude: true} // 添加一个 meta 字段表示这个组件应该被排除
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

const adminPages = ["AdminDownload", "AdminCourse", "AdminCategory",
    "AdminColumn", "AdminQuestion", "AdminWxQuestion", "AdminWxSpecialPage", "AdminUsers", "AdminConfig"];

router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    // 支付页面
    if (to.name === 'Paying' && from.name !== 'Pay') {
        // 如果目标页面是TargetPage，但来源页面不是SourcePage，则重定向到来源页面
        next({name: 'Pay'});
        return;
    }

    // 管理页面
    if (adminPages.includes(String(to.name)) && from.name === 'Admin' && from.name === to.name) {
        next();
        return;
    }

    // 其他页面通通放行
    next();
});

export default router
