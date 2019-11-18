<template>
    <v-container>
        <v-row justify="center" align="center">
            <v-col cols="12">
                <v-card elevation="1" width="100%">
                    <v-card-title>
                        <v-text-field
                            outlined
                            rounded
                            label="Name"
                            @keyup.native="textChanged"
                            v-model="computedSelectedNote.name"
                        ></v-text-field>
                    </v-card-title>
                    <v-card-subtitle></v-card-subtitle>
                    <v-card-text>
                        <vue-editor
                            @text-change="textChanged"
                            v-model="computedSelectedNote.body"
                            :editorToolbar="customToolbar"
                        ></vue-editor>
                    </v-card-text>
                    <v-card-actions>
                        <v-label>Last saved {{getRelativeUpdateTime(computedSelectedNote.lastUpdatedTimestamp)}}</v-label>
                        <v-spacer />
                        <v-btn @click="shareOverlay = !shareOverlay" small rounded text>
                            <v-icon color="primary">mdi-share-variant</v-icon>
                        </v-btn>
                        <v-btn @click="deleteOverlay = !deleteOverlay" small rounded text>
                            <v-icon color="primary">mdi-delete</v-icon>
                        </v-btn>

                        <v-overlay v-model="shareOverlay" absolute>
                            <v-card light>
                                <v-card-text>Sharing '{{computedSelectedNote.name}}'?</v-card-text>
                                <v-card-actions>
                                    <v-btn class="font-weight-bold" color="primary" @click.stop="deleteNote" small rounded>Yes</v-btn>
                                    <v-spacer />
                                    <v-btn class="font-weight-bold" color="primary" @click="deleteOverlay = !deleteOverlay" small rounded>No</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-overlay>

                        <v-overlay v-model="deleteOverlay" absolute>
                            <v-card light>
                                <v-card-text>Delete '{{computedSelectedNote.name}}'?</v-card-text>
                                <v-card-actions>
                                    <v-btn class="font-weight-bold" color="primary" @click.stop="deleteNote" small rounded>Yes</v-btn>
                                    <v-spacer />
                                    <v-btn class="font-weight-bold" color="primary" @click="deleteOverlay = !deleteOverlay" small rounded>No</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-overlay>

                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import { VueEditor } from "vue2-editor";
import moment from "moment";
import { mapState } from "vuex";

export default {
    components: { VueEditor },

    data() {
        return {
            customToolbar: [
                ["bold", "italic", "underline"],
                [{ list: "ordered" }, { list: "bullet" }],
                ["code-block"]
            ],
            deleteOverlay: false,
            shareOverlay: false
        };
    },

    computed: {
        ...mapState({
            notes: state => state.notes,
            selectedNoteUuid: state => state.selectedNoteUuid,
            ignoreNextEditEvent: state => state.ignoreNextEditEvent
        }),

        computedSelectedNote() {
            var selectedNote = this.notes.filter(
                note => note.uuid == this.selectedNoteUuid
            )[0];
            if (selectedNote == null) {
                return {
                    name: "Loading..."
                };
            }
            return selectedNote;
        }
    },

    watch: {
        $route(to, from) {
            // react to route changes...
            console.log("Going from " + from + " to " + to);
        },
        selectedNoteUuid() {
            this.deleteOverlay = false;
            this.shareOverlay = false;
        }
    },

    methods: {
        getRelativeUpdateTime(time) {
            return moment(time).fromNow();
        },
        textChanged() {
            if (this.ignoreNextEditEvent) {
                this.$store.commit("setIgnoreNextEditEvent", false);
            } else {
                this.$store.commit("addUnsavedNote", {
                    uuid: this.selectedNoteUuid,
                    time: Date.now()
                });
            }
        },
        deleteNote() {
            this.$store.commit("deleteNote", this.selectedNoteUuid)
        }
    }
};
</script>