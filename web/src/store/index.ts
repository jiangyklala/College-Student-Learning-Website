import { createStore } from 'vuex'
import {SessionStorage} from "@/utils/SessionStorage";


const USERINFO = "USERINFO";

const store = createStore({
  state: {
    userInfo: SessionStorage.get(USERINFO) || {}
  },
  getters: {
  },
  mutations: {
    setUserInfo (state, userInfo) {
      state.userInfo = userInfo;
      SessionStorage.set(USERINFO, userInfo)
    }
  },
  actions: {
  },
  modules: {
  }
})

export default store;