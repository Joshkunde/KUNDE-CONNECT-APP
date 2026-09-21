package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.Language
import com.example.ui.components.KundeBottomBar
import com.example.ui.components.KundeTopBar
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun kunde_topbar_screenshot() {
        composeTestRule.setContent {
            MyApplicationTheme {
                KundeTopBar(
                    language = Language.FRANCAIS,
                    isDataSaver = true,
                    isOnline = true,
                    unreadNotificationsCount = 3,
                    onToggleDataSaver = {},
                    onOpenLanguage = {},
                    onOpenNotifications = {},
                    onToggleOnline = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/kunde_topbar.png")
    }

    @Test
    fun kunde_bottombar_screenshot() {
        composeTestRule.setContent {
            MyApplicationTheme {
                KundeBottomBar(
                    selectedIndex = 0,
                    language = Language.FRANCAIS,
                    onSelectTab = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/kunde_bottombar.png")
    }
}
