import Vue from 'vue';
import Vuetify from 'vuetify/lib';


Vue.use(
  Vuetify
);

export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: '#1C90EF',
        secondary: '#b0bec5',
        accent: '#8c9eff',
        error: '#b71c1c',
      },
    },
  },
  icons: {
    iconfont: 'mdi',
  }
});