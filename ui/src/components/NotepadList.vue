<template>
    <div>
        <vue-headful :title="computedPageTitle" />

        <v-app-bar app clipped-left clipped-right>
            <v-btn small rounded text @click.stop="drawer = !drawer">
                <v-icon>mdi-menu</v-icon>
            </v-btn>
            <v-toolbar-title class="mr-12 align-center">
                <span class="title">MNotes</span>
            </v-toolbar-title>
            <v-spacer />
            <v-btn small rounded text @click.stop="drawerRight = !drawerRight">
                <v-icon v-if="drawerRight">mdi-arrow-right</v-icon>
                <v-icon v-if="!drawerRight">mdi-arrow-left</v-icon>
            </v-btn>
        </v-app-bar>

        <v-navigation-drawer v-model="drawer" app clipped>
            <v-list dense>
                <v-list-item>
                    <v-list-item-title class="grey--text">NOTEPADS</v-list-item-title>
                    <v-btn @click.stop="createNotepad" rounded text small>
                        <v-icon>mdi-file-plus-outline</v-icon>
                    </v-btn>
                </v-list-item>
                <v-list-item
                    @click.stop="selectNotepad(notepad)"
                    v-for="notepad in sortedNotepads"
                    :key="notepad.uuid"
                    link
                    :class="`elevation-${notepad.uuid == selectedNotepadUuid ? 2 : 0}` "
                    dense
                >
                    <v-list-item-title class="grey--text text--darken-1">
                        <span v-if="notepadHasBeenEdited(notepad.uuid)">*</span>
                        {{notepad.name}}
                    </v-list-item-title>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-navigation-drawer v-model="drawerRight" app clipped right>
            <v-list dense>
                <v-list-item>
                    <p>Notepad has been edited: {{notepadHasBeenEdited(computedSelectedNotepad.uuid)}}</p>
                </v-list-item>
                <v-divider></v-divider>
                <v-btn @click.stop="logout">Logout</v-btn>
            </v-list>
        </v-navigation-drawer>

        <v-container>
            <v-row justify="center" align="center">
                <v-col cols="12">
                    <v-card elevation="1" width="100%">
                        <v-card-title>
                            <v-text-field
                                outlined
                                flat
                                rounded
                                solo
                                @keyup.native="textChanged"
                                v-model="computedSelectedNotepad.name"
                            ></v-text-field>
                        </v-card-title>
                        <v-card-subtitle></v-card-subtitle>
                        <v-card-text>
                            <vue-editor
                                @text-change="textChanged"
                                v-model="computedSelectedNotepad.body"
                                :editorToolbar="customToolbar"
                            ></vue-editor>
                        </v-card-text>
                        <v-card-actions>
                            <v-label>Last saved {{getRelativeUpdateTime(computedSelectedNotepad.lastUpdatedTimestamp)}}</v-label>
                            <v-spacer />
                            <v-btn @click="deleteOverlay = !deleteOverlay" small rounded text>
                                <v-icon>mdi-delete</v-icon>
                            </v-btn>
                            <v-overlay v-model="deleteOverlay" absolute>
                                <v-card>
                                    <v-card-text>Delete '{{computedSelectedNotepad.name}}'?</v-card-text>
                                    <v-card-actions>
                                        <v-btn @click.stop="deleteNotepad" small rounded>Yes</v-btn>
                                        <v-spacer />
                                        <v-btn
                                            @click="deleteOverlay = !deleteOverlay"
                                            small
                                            rounded
                                        >No</v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-overlay>
                        </v-card-actions>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../config.js";
import { VueEditor } from "vue2-editor";
import { setInterval } from "timers";
import moment from "moment";
import short from "short-uuid";

var translator = short(); 

export default {
    components: { VueEditor },

    props: {
        source: String
    },

    data() {
        return {
            drawer: null,
            drawerRight: false,
            content: "",
            fab: "",
            customToolbar: [
                ["bold", "italic", "underline"],
                [{ list: "ordered" }, { list: "bullet" }],
                ["code-block"]
            ],
            selectedNotepadUuid: 1,
            notepads: [],
            unsavedNotepads: {},
            ignoreNextEditEvent: true,
            pageHasBeenEdited: false,
            deleteOverlay: false
        };
    },

    computed: {
        sortedNotepads() {
            return this.notepads.slice(0).sort(function(x, y) {
                return y.lastUpdatedTimestamp - x.lastUpdatedTimestamp;
            });
        },
        computedSelectedNotepad() {
            var selectedNotepad = this.notepads.filter(
                notepad => notepad.uuid === this.selectedNotepadUuid
            )[0];

            if (selectedNotepad == null) {
                return {
                    uuid: 0,
                    name: "Loading..."
                };
            }
            return selectedNotepad;
        },

        computedPageTitle() {
            var title = "MNotes | " + this.computedSelectedNotepad.name;
            if (this.pageHasBeenEdited) {
                title = "* " + title;
            }
            return title;
        }
    },

    watch: {
        $route(to, from) {
            // react to route changes...
            console.log("Going from " + from + " to " + to);
        }
    },

    mounted() {
        this.getNotepads();
        //start autosave timer
        setInterval(this.saveUpdatedNotepads, 2000);
    },

    methods: {
        getRelativeUpdateTime(time) {
            return moment(time).fromNow();
        },

        notepadHasBeenEdited(notepadUuid) {
            return this.unsavedNotepads[notepadUuid] != null;
        },

        selectNotepad(notepad) {
            this.ignoreNextEditEvent = true;
            this.deleteOverlay = false;

            var newRoutePath = "/notes/" + translator.fromUUID(notepad.uuid);
            if (this.$route.path != newRoutePath) {
                this.$router.replace(newRoutePath);
            }

            this.selectedNotepadUuid = notepad.uuid;
        },

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

        textChanged() {
            if (this.ignoreNextEditEvent) {
                this.ignoreNextEditEvent = false;
            } else {
                this.pageHasBeenEdited = true;
                this.unsavedNotepads[this.computedSelectedNotepad.uuid] = {
                    time: Date.now()
                };
            }
        },

        setSelectedNotepad() {
            if (this.notepads[0] == null) {
                this.createNotepad();
            } else {
                var path = this.$route.path;
                var notepadId = path.replace("/notes", "");

                if (notepadId == "") {
                    this.selectNotepad(this.notepads[0]);
                    return;
                }

                notepadId = notepadId.replace("/", "");

                if (notepadId == "") {
                    this.selectNotepad(this.notepads[0]);
                    return;
                }

                notepadId = translator.toUUID(notepadId);

                var notepadToRouteTo = this.notepads.filter(
                    notepad => notepad.uuid == notepadId
                )[0];
                if (notepadToRouteTo == null) {
                    this.selectNotepad(this.notepads[0]);
                } else {
                    console.log("Routing to notepad: " + notepadToRouteTo.uuid);
                    this.selectNotepad(notepadToRouteTo);
                }
            }
        },

        createNotepad() {
            var createNotepadRequest = {
                name: "Untitled notepad"
            };
            axios
                .put(SERVER_URL + "/notepads", createNotepadRequest)
                .then(response => {
                    this.notepads.push(response.data);
                    this.setSelectedNotepad();
                })
                .catch(error => {
                    this.handleApiError(error);
                });
        },

        getNotepads() {
            axios
                .get(SERVER_URL + "/notepads")
                .then(response => {
                    this.notepads = response.data;
                    this.setSelectedNotepad();
                })
                .catch(error => {
                    this.handleApiError(error);
                });
        },

        saveNotepad(notepadToSave) {
            delete this.unsavedNotepads[notepadToSave.uuid];
            axios
                .post(
                    SERVER_URL + "/notepads/" + notepadToSave.uuid,
                    notepadToSave
                )
                .then(response => {
                    this.pageHasBeenEdited = false;
                    this.notepads = this.notepads.filter(
                        notepad => notepad.uuid != notepadToSave.uuid
                    );
                    this.notepads.push(response.data);
                })
                .catch(error => {
                    this.handleApiError(error);
                });
        },

        deleteNotepad() {
            this.deleteOverlay = false;
            axios
                .delete(SERVER_URL + "/notepads/" + this.selectedNotepadUuid)
                .then(response => {
                    if (response != null) {
                        this.notepads = this.notepads.filter(
                            notepad => notepad.uuid != this.selectedNotepadUuid
                        );
                        this.setSelectedNotepad();
                    }
                })
                .catch(error => {
                    this.handleApiError(error);
                });
        },

        logout() {
            axios
                .post(SERVER_URL + "/logout")
                .then(response => {
                    response;
                    this.$router.replace("/login");
                })
                .catch(error => {
                    this.handleApiError(error);
                });
        },

        handleApiError(error) {
            console.log(error);
            this.$router.replace("/login");
        }
    }
};
</script>