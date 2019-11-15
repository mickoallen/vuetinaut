<template>
    <div>
        <v-app-bar app clipped-left clipped-right>
            <v-btn small rounded text @click.stop="noteNavigation = !noteNavigation">
                <v-icon>mdi-menu</v-icon>
            </v-btn>
            <v-toolbar-title class="mr-12 align-center">
                <span class="title">MNotes</span>
            </v-toolbar-title>
            <v-spacer />
            <v-btn small rounded text @click.stop="infoPanel = !infoPanel">
                <v-icon v-if="infoPanel">mdi-arrow-right</v-icon>
                <v-icon v-if="!infoPanel">mdi-arrow-left</v-icon>
            </v-btn>
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
        getSnackColor() {
            return this.snackError ? "error" : "success";
        }
    },

    methods: {
        saveUpdatedNotepads() {
            for (
                var i = 0,
                    keys = Object.keys(this.unsavedNotepads),
                    numberOfUnsavedNotepads = keys.length;
                i < numberOfUnsavedNotepads;
                i++
            ) {
                var editedNotepad = this.unsavedNotepads[keys[i]];
                var now = Date.now();

                //safe if 2 seconds has passed since the last edit
                if (now > editedNotepad.time + 2000) {
                    this.saveNotepad(
                        this.notepads.filter(
                            notepad => notepad.uuid == keys[i]
                        )[0]
                    );
                }
            }
        },
    },

    mounted() {
        this.$store.commit("loadNotes");
    }
};
</script>
