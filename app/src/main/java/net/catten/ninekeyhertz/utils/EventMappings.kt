package net.catten.ninekeyhertz.utils

object EventMappings {
    enum class Event(val description: String, val value: Int) {
        TYPE_VIEW_CLICKED(""" Represents the event of clicking on a {@link android.view.View} like {@link android.widget.Button}, {@link android.widget.CompoundButton}, etc.""".trimIndent(), 0x00000001),
        TYPE_VIEW_LONG_CLICKED(""" Represents the event of long clicking on a {@link android.view.View} like {@link android.widget.Button}, {@link android.widget.CompoundButton}, etc.""".trimIndent(), 0x00000002),
        TYPE_VIEW_SELECTED(""" Represents the event of selecting an item usually in the context of an {@link android.widget.AdapterView}.""".trimIndent(), 0x00000004),
        TYPE_VIEW_FOCUSED(""" Represents the event of setting input focus of a {@link android.view.View}.""".trimIndent(), 0x00000008),
        TYPE_VIEW_TEXT_CHANGED(""" Represents the event of changing the text of an {@link android.widget.EditText}.""".trimIndent(), 0x00000010),
        TYPE_WINDOW_STATE_CHANGED(""" Represents the event of a change to a visually distinct section of the user interface. These events should only be dispatched from {@link android.view.View}s that have accessibility pane titles, and replaces {@link #TYPE_WINDOW_CONTENT_CHANGED} for those sources. Details about the change are available from {@link #getContentChangeTypes()}.""".trimIndent(), 0x00000020),
        TYPE_NOTIFICATION_STATE_CHANGED(""" Represents the event showing a {@link android.app.Notification}.""".trimIndent(), 0x00000040),
        TYPE_VIEW_HOVER_ENTER(""" Represents the event of a hover enter over a {@link android.view.View}.""".trimIndent(), 0x00000080),
        TYPE_VIEW_HOVER_EXIT(""" Represents the event of a hover exit over a {@link android.view.View}.""".trimIndent(), 0x00000100),
        TYPE_TOUCH_EXPLORATION_GESTURE_START(""" Represents the event of starting a touch exploration gesture.""".trimIndent(), 0x00000200),
        TYPE_TOUCH_EXPLORATION_GESTURE_END(""" Represents the event of ending a touch exploration gesture.""".trimIndent(), 0x00000400),
        TYPE_WINDOW_CONTENT_CHANGED(""" Represents the event of changing the content of a window and more specifically the sub-tree rooted at the event's source.""".trimIndent(), 0x00000800),
        TYPE_VIEW_SCROLLED(""" Represents the event of scrolling a view. This event type is generally not sent directly. @see View#onScrollChanged(int, int, int, int""".trimIndent(), 0x00001000),
        TYPE_VIEW_TEXT_SELECTION_CHANGED(""" Represents the event of changing the selection in an {@link android.widget.EditText}.""".trimIndent(), 0x00002000),
        TYPE_ANNOUNCEMENT(""" Represents the event of an application making an announcement.""".trimIndent(), 0x00004000),
        TYPE_VIEW_ACCESSIBILITY_FOCUSED(""" Represents the event of gaining accessibility focus.""".trimIndent(), 0x00008000),
        TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED(""" Represents the event of clearing accessibility focus.""".trimIndent(), 0x00010000),
        TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY(""" Represents the event of traversing the text of a view at a given movement granularity.""".trimIndent(), 0x00020000),
        TYPE_GESTURE_DETECTION_START(""" Represents the event of beginning gesture detection.""".trimIndent(), 0x00040000),
        TYPE_GESTURE_DETECTION_END(""" Represents the event of ending gesture detection.""".trimIndent(), 0x00080000),
        TYPE_TOUCH_INTERACTION_START(""" Represents the event of the user starting to touch the screen.""".trimIndent(), 0x00100000),
        TYPE_TOUCH_INTERACTION_END(""" Represents the event of the user ending to touch the screen.""".trimIndent(), 0x00200000),
        TYPE_WINDOWS_CHANGED(""" Represents the event change in the system windows shown on the screen. This event type should only be dispatched by the system.""".trimIndent(), 0x00400000),
        TYPE_VIEW_CONTEXT_CLICKED(""" Represents the event of a context click on a {@link android.view.View}.""".trimIndent(), 0x00800000),
        TYPE_ASSIST_READING_CONTEXT(""" Represents the event of the assistant currently reading the users screen context.""".trimIndent(), 0x01000000),

        UNKNOWN("""UNKNOWN TYPE""".trimIndent(), -1)

    }

    private val reverseMapping = Event.values().map { Pair(it.value,it) }.toMap()

    fun eventOf(value : Int) : Event = reverseMapping[value]?: Event.UNKNOWN
}