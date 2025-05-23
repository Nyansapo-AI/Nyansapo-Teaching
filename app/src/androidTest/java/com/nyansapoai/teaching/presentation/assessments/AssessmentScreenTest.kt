package com.nyansapoai.teaching.presentation.assessments

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class AssessmentScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

//    lateinit var navController: TestNavHostController

    @Test
    fun assessmentScreen_titleDisplayed() {
        composeTestRule.setContent {
            AssessmentsScreen(
                state = AssessmentsState(),
                onAction = {}
            )
        }

        composeTestRule.onNodeWithText("Assessment").assertExists()
        composeTestRule.onNodeWithText("Add Assessment").assertExists()
    }

    /*
    @Before
    fun setUp() {

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        navController.navigatorProvider.addNavigator(
            ComposeNavigator()
        )

        composeTestRule.setContent {
            NavHost(
                navController = navController,
                startDestination = "assessments",
            ){
                composable("assessments") {
                    AssessmentsScreen(
                        state = AssessmentsState(),
                        onAction = {}
                    )
                }

                composable("CreateAssessment") {
                    CreateAssessmentsScreen(
                        state = CreateAssessmentsState(),
                        onAction = {}
                    )
                }
            }
        }
    }*/

    @Test
    fun addAssessmentScreen_show_whenButtonClicked() {

        composeTestRule.setContent {
            AssessmentsScreen(
                state = AssessmentsState(),
                onAction = {}
            )
        }

        composeTestRule.onNodeWithText("Add Assessment")
            .assertIsEnabled()
            .assertHasClickAction()
    }

}