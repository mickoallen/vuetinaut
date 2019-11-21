<template>
    <div>
        <v-list dense>
            <v-list-item link @click.stop="createNote">
                <v-list-item-title class="font-weight-bold">CREATE NEW NOTE</v-list-item-title>
                <v-icon color="primary">mdi-file-plus-outline</v-icon>
            </v-list-item>
            <v-list-item
                @click.stop="selectNote(note)"
                v-for="note in sortedNotes"
                :key="note.uuid"
                link
                :class="`elevation-${note.uuid == selectedNoteUuid ? 2 : 0}` "
                dense
            >
                <v-list-item-title>
                    <span v-if="noteIsUnsaved(note.uuid)">*</span>
                    {{note.name}}
                </v-list-item-title>
            </v-list-item>
        </v-list>
    </div>
</template>

<script>
import { mapState } from "vuex";

export default {
    computed: {
        ...mapState({
            notes: state => state.notes,
            selectedNoteUuid: state => state.selectedNoteUuid,
            unsavedNotes: state => state.unsavedNotes,
            currentUser: state => state.currentUser
        }),

        sortedNotes() {
            return this.notes.slice(0).sort(function(x, y) {
                return y.lastUpdatedTimestamp - x.lastUpdatedTimestamp;
            });
        }
    },

    methods: {
        noteIsUnsaved(noteUuid) {
            return (
                this.unsavedNotes.filter(note => note.uuid == noteUuid)[0] !=
                null
            );
        },
        selectNote(note) {
            this.$store.commit("selectNote", note);
        },
        createNote() {
            this.$store.commit("createNote");
        }
    }
};
</script>