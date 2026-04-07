package com.foxtask.app.presentation.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Tasks : Screen("tasks")
    object Shop : Screen("shop")
    object Wardrobe : Screen("wardrobe")
    object Stats : Screen("stats")
    object TaskEdit : Screen("task_edit/{taskId}") {
        fun createRoute(taskId: Int? = null) = 
            if (taskId == null) "task_edit" else "task_edit/$taskId"
    }
}
