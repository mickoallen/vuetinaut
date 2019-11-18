<template>
    <div>
        <vue-headful :title="pageTitle" />

        <v-app-bar dense app clipped-left clipped-right>
            <v-btn small rounded text @click.stop="noteNavigation = !noteNavigation">
                <v-icon color="primary" >mdi-menu</v-icon>
            </v-btn>
            <v-toolbar-title class="mr-12 align-center">
                <span class="title">MNotes</span>   
            </v-toolbar-title>
            <v-spacer />
            <v-btn small rounded @click.stop="logout" class="font-weight-bold" color="primary">Logout</v-btn>
            <!-- <v-btn small rounded text @click.stop="infoPanel = !infoPanel">
                <v-icon color="primary" v-if="infoPanel">mdi-arrow-right</v-icon>
                <v-icon color="primary" v-if="!infoPanel">mdi-arrow-left</v-icon>
            </v-btn> -->
        </v-app-bar>

        <v-navigation-drawer v-model="noteNavigation" app clipped>
            <NoteNavigation />
        </v-navigation-drawer>

        <v-navigation-drawer v-model="infoPanel" app clipped right>
            <InfoPanel />
        </v-navigation-drawer>

        <NoteEditor />
    </div>
</template>

<script>
import NoteNavigation from "../components/NoteNavigation.vue";
import NoteEditor from "../components/NoteEditor.vue";
import InfoPanel from "../components/InfoPanel.vue";
import { mapState } from "vuex";
import { setInterval } from "timers";

export default {
    components: {
        NoteNavigation,
        NoteEditor,
        InfoPanel
    },

    data() {
        return {
            noteNavigation: true,
            infoPanel: false,
            notepads: [],
            showSnack: false,
            snackError: false,
            snackMessage: ""
        };
    },

    computed: {
        ...mapState({
            notes: state => state.notes,
            selectedNoteUuid: state => state.selectedNoteUuid,
            unsavedNotes: state => state.unsavedNotes
        }),
        pageTitle() {
            if (this.selectedNoteUuid == null || this.notes.length == 0) {
                return "MNotes";
            }

            var pageTitle =
                "MNotes | " +
                this.notes.filter(note => note.uuid == this.selectedNoteUuid)[0]
                    .name;

            if (
                this.unsavedNotes.filter(
                    note => note.uuid == this.selectedNoteUuid
                ).length == 1
            ) {
                pageTitle = "* " + pageTitle;
            }
            return pageTitle;
        }
    },

    methods: {
        saveUnsavedNotes() {
            this.unsavedNotes.forEach(unsavedNote => {
                var now = Date.now();

                //safe if 2 seconds has passed since the last edit
                if (now > unsavedNote.time + 2000) {
                    this.saveNote(
                        this.notes.filter(
                            note => note.uuid == unsavedNote.uuid
                        )[0]
                    );
                }
            });
        },

        saveNote(note) {
            this.$store.commit("saveNote", note);
        },

        logout() {
            this.$store.commit("logout");
        }
    },

    mounted() {
        this.$store.commit("loadNotes");
        this.$store.commit("getCurrentUser");
        setInterval(this.saveUnsavedNotes, 2000);
    }
};
</script>
