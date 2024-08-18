import { createRouter, createWebHistory } from "vue-router";
import IntroPage from '@/views/IntroPage.vue'

import Home from "@/views/Home.vue";
import NotFound from "@/views/NotFound.vue";
import routerActivity from "@/views/Activity/router-activity";
import routerPersonal from "@/views/Personal/router-personal";
import routerEdit from "@/views/Edit/router-edit";
import routerSearch from "@/views/Search/router-search";
import routerShopping from "@/views/Shopping/router-shopping";
import routerLogin from "@/views/Login/router-login";
import routerMessage from "@/views/Message/router-message";
import routerPost from "@/views/Post/router-post";
import routerFriendPersonal from '@/views/Friend/router-personal';
import david from "@/views/david-view/david-view"
import routerSubs from '@/views/Subscriptions/router-subscriptions';//(Sean240729)訂閱
import BombPage from '@/views/Friend/BombPage.vue'; // 給輪播
import routerManagement from "@/views/GM/router-management";

const routes = [
    { name: "home-link", path: "/home", component: Home },
    { name: "intropage-link", path: "/", component: IntroPage },
    { name: "notfound-link", path: "/:pathMatch(.*)*", component: NotFound },
    { path: '/views/Bomb', name: 'bomb', component: BombPage },
    ...routerActivity,
    ...routerPersonal,
    ...routerEdit,
    ...routerShopping,
    ...routerSearch,
    ...routerLogin,
    ...routerMessage,
    ...routerPost,
    ...routerFriendPersonal,
    ...david,
    ...routerSubs, //(Sean240729)訂閱
    ...routerManagement,
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes,
})
router.beforeEach((to, from, next) => {
    const requiresStatus2 = to.meta.requiresStatus2;
    const userStatus = sessionStorage.getItem('user-status'); // 從 sessionStorage 取得使用者狀態

    if (requiresStatus2) {
        if (userStatus === '2') {
            next(); // 使用者狀態為2，允許訪問
        } else {
            next('/notfound'); // 否則重新導向到404頁面
        }
    } else {
        next(); // 不需要狀態2的頁面，允許訪問
    }
});

export default router;