package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppStrings
import com.example.data.model.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Kunde Connect", appName)
    }

    @Test
    fun `verify multilingual translations exist`() {
        val frenchFeed = AppStrings.get("tab_feed", Language.FRANCAIS)
        val lingalaFeed = AppStrings.get("tab_feed", Language.LINGALA)
        val englishFeed = AppStrings.get("tab_feed", Language.ENGLISH)

        assertEquals("Accueil", frenchFeed)
        assertEquals("Ndako", lingalaFeed)
        assertEquals("Home", englishFeed)

        val frenchFaith = AppStrings.get("filter_faith", Language.FRANCAIS)
        val lingalaFaith = AppStrings.get("filter_faith", Language.LINGALA)
        assertEquals("Foi", frenchFaith)
        assertEquals("Bondimi", lingalaFaith)
    }

    @Test
    fun `verify language models`() {
        val languages = listOf(Language.FRANCAIS, Language.LINGALA, Language.ENGLISH)
        assertEquals(3, languages.size)
        assertTrue(languages.any { it.code == "ln" })
    }

    @Test
    fun `verify default chat conversations from flutter specification`() {
        val chats = com.example.ui.screens.defaultChats
        assertEquals(6, chats.size)

        val coordinationIturi = chats.firstOrNull { it.name == "Coordination Ituri" }
        assertNotNull(coordinationIturi)
        assertTrue(coordinationIturi!!.isGroup)
        assertEquals(3, coordinationIturi.unread)
        assertEquals("IT", coordinationIturi.initials)

        val groups = chats.filter { it.isGroup }
        assertEquals(3, groups.size)

        val unreadChats = chats.filter { it.unread > 0 }
        assertEquals(3, unreadChats.size)
    }

    @Test
    fun `verify SettingsScreen renders without crash`() {
        var backClicked = false
        composeTestRule.setContent {
            com.example.ui.screens.SettingsScreen(
                onBack = { backClicked = true }
            )
        }
        composeTestRule.onNodeWithTag("settings_screen").assertExists()
        composeTestRule.onNodeWithTag("settings_title").assertExists()
        composeTestRule.onNodeWithTag("profile_tile").assertExists()
        composeTestRule.onNodeWithTag("qr_code_btn").assertExists()
        composeTestRule.onNodeWithTag("settings_tile_compte").assertExists()
        composeTestRule.onNodeWithTag("settings_tile_confidentialité").assertExists()
        composeTestRule.onNodeWithTag("settings_tile_discussions").assertExists()
        composeTestRule.onNodeWithTag("settings_tile_notifications").assertExists()
        composeTestRule.onNodeWithTag("settings_tile_kunde_société").assertExists()

        composeTestRule.onNodeWithTag("settings_back_btn").performClick()
        assertTrue(backClicked)
    }

    @Test
    fun `verify CallScreen renders with video controls and bottom buttons`() {
        var callEnded = false
        composeTestRule.setContent {
            com.example.ui.screens.CallScreen(
                channelName = "Familia_Ituri",
                isVideo = true,
                onEndCall = { callEnded = true }
            )
        }
        composeTestRule.onNodeWithTag("call_screen").assertExists()
        composeTestRule.onNodeWithTag("call_btn_mic").assertExists()
        composeTestRule.onNodeWithTag("call_btn_video").assertExists()
        composeTestRule.onNodeWithTag("call_btn_screen_share").assertExists()
        composeTestRule.onNodeWithTag("call_btn_add_person").assertExists()
        composeTestRule.onNodeWithTag("call_btn_end").assertExists()

        // Toggle mic button
        composeTestRule.onNodeWithTag("call_btn_mic").performClick()
        // Toggle screen share button
        composeTestRule.onNodeWithTag("call_btn_screen_share").performClick()

        // End call
        composeTestRule.onNodeWithTag("call_btn_end").performClick()
        assertTrue(callEnded)
    }
}
