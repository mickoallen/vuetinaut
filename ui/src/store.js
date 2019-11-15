import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios";
import { SERVER_URL } from "./config.js";
import short from "short-uuid";
import router from './router'
// import { setInterval } from "timers";

const translator = short();
Vue.use(Vuex);
// setInterval(this.saveUpdatedNotepads, 2000);

export default new Vuex.Store({
  state: {
    selectedNoteUuid: 1,
    notes: [],
    unsavedNotes: [],
    ignoreNextEditEvent: true,
    noteHasBeenEdited: false,
  },
  mutations: {
    loadNotes(state) {
      axios
        .get(SERVER_URL + "/notepads")
        .then(response => {
          state.notes = response.data;
          this.commit("setSelectedNote");
        })
        .catch(error => {
          console.log(error);
        });
    },

    selectNote(state, note) {
      state.ignoreNextEditEvent = true;

      var newRoutePath = "/notes/" + translator.fromUUID(note.uuid);
      if (router.currentRoute.path != newRoutePath) {
        router.replace(newRoutePath);
      }

      state.selectedNoteUuid = note.uuid;
    },

    setSelectedNote(state) {
      if (state.notes[0] == null) {
        state.createNote();
      } else {
        var noteId = router.currentRoute.path.replace("/notes", "");

        if (noteId == "") {
          this.commit("selectNote", state.notes[0]);
          return;
        }

        noteId = noteId.replace("/", "");

        if (noteId == "") {
          this.commit("selectNote", state.notes[0]);
          return;
        }

        noteId = translator.toUUID(noteId);

        var noteToRouteTo = state.notes.filter(
          note => note.uuid == noteId
        )[0];

        if (noteToRouteTo == null) {
          this.commit("selectNote", state.notepads[0]);
        } else {
          this.commit("selectNote", noteToRouteTo);
        }
      }
    },

    createNote(state) {
      axios
        .put(SERVER_URL + "/notepads", {
          name: "Untitled notepad"
        })
        .then(response => {
          state.notes.push(response.data);
          this.commit("selectNote", response.data);
        })
        .catch(error => {
          console.log(error);
        });
    },

    setIgnoreNextEditEvent(state, doIgnore) {
      state.ignoreNextEditEvent = doIgnore;
    },

    addUnsavedNote(state, unsavedNoteEvent) {
      let indexOfExistingEdit = state.unsavedNotes.map(note => note.uuid).indexOf(unsavedNoteEvent.uuid);

      if (indexOfExistingEdit > -1) {
        state.unsavedNotes.splice(indexOfExistingEdit, 1);
      }

      state.unsavedNotes.push(unsavedNoteEvent);
    }

  },
  actions: {

  }
})
