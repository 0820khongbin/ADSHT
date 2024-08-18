import { createApp } from 'vue';
import App from './App.vue';
import router from './router/router.js';
import FontAwesomeIcon from './plugins/fontawesome.js';
import 'semantic-ui-css/semantic.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'animate.css';
import VueLazyload from 'vue-lazyload';

const app = createApp(App);

app.use(router);
app.use(VueLazyload, {
    preLoad: 1.3,
    error: '/loading/error.jpg',
    loading: '/loading/loading.gif',
    attempt: 1
});

app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app');