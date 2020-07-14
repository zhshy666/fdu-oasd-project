import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        token: localStorage.getItem('token') || null,
        cur_user: localStorage.getItem('username') || null
    },
    mutations: {
        login(state, data) {
            localStorage.setItem('token', data.token)
            localStorage.setItem('username', data.username)
            state.token = data.token
            state.cur_user = data.username
        },
        logout(state) {
            // releasetoken
            localStorage.removeItem('token')
            localStorage.removeItem('username')
            state.token = null
            state.cur_user = null
        }
    },
    actions: {}
})