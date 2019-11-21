import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios";
import { SERVER_URL } from "./config.js";
import short from "short-uuid";
import router from './router';

const translator = short();
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    currentUser: {
      uuid: "",
      username: "",
      userType: ""
    },
    selectedNoteUuid: 1,
    notes: [],
    unsavedNotes: [],
    ignoreNextEditEvent: true,
    noteHasBeenEdited: false,
    deleteOverlay: false,

    successMessage: { message: null, time: null },
    errorMessage: { message: null, time: null }
  },
  mutations: {
    getCurrentUser(state) {
      axios
        .get(SERVER_URL + "/users/current")
        .then(response => {
          state.currentUser = response.data;
        })
        .catch(error => {
          this.commit("apiError", error);
        });
    },

    loadNotes(state) {
      axios
        .get(SERVER_URL + "/notepads")
        .then(response => {
          state.notes = response.data;
          this.commit("setSelectedNote");
        })
        .catch(error => {
          this.commit("apiError", error);
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
        this.commit("createNote");
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
          this.commit("selectNote", state.notes[0]);
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
          this.commit("successSnackbar", "Created " + response.data.name);
        })
        .catch(error => {
          this.commit("apiError", error);
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
    },

    saveNote(state, noteToSave) {
      //only save if name is valid
      if (noteToSave.name == null || noteToSave.name == "") {
        return;
      }

      let indexOfNote = state.unsavedNotes.map(note => note.uuid).indexOf(noteToSave.uuid);
      state.unsavedNotes.splice(indexOfNote, 1);

      axios
        .post(
          SERVER_URL + "/notepads/" + noteToSave.uuid,
          noteToSave
        )
        .then(response => {
          let indexOfSavedNote = state.notes.map(note => note.uuid).indexOf(noteToSave.uuid);
          state.notes.splice(indexOfSavedNote, 1);
          state.notes.push(response.data);
        })
        .catch(error => {
          this.commit("apiError", error);
        });
    },

    deleteNote(state, noteUuidToDelete) {
      axios
        .delete(SERVER_URL + "/notepads/" + noteUuidToDelete)
        .then(response => {
          if (response != null) {
            let indexOfDeletedNote = state.notes.map(note => note.uuid).indexOf(noteUuidToDelete);
            state.notes.splice(indexOfDeletedNote, 1);

            this.commit("setSelectedNote");
            this.commit("successSnackbar", "Deleted");
          }
        })
        .catch(error => {
          this.commit("apiError", error);
        });
    },

    logout(state) {
      state.selectedNoteUuid = null;
      state.notes = [];
      state.unsavedNotes = [];
      state.currentUser = {};
      axios
        .post(SERVER_URL + "/logout")
        .then(response => {
          response;
          router.replace("/login");
        })
        .catch(error => {
          this.commit("apiError", error);
        });
    },

    successSnackbar(state, message) {
      state.successMessage = { message: message, time: Date.now() };
    },

    errorSnackbar(state, message) {
      state.errorMessage = { message: message, time: Date.now() };
    },

    apiError(state, error) {
      if (error.response.status == 401) {
        if (router.currentRoute.path != "/login") {
          this.commit("errorSnackbar", "Login required");
          router.replace("/login");
        }
      } else {
        this.commit("errorSnackbar", "Something went wrong");
      }
    }
  },
  actions: {

  }
})
