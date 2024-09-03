package com.thedev.mercadolivre.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thedev.mercadolivre.features.home.HomeRoute
import com.thedev.mercadolivre.features.product.ProductRoute

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "search") {
        composable("search") {
            HomeRoute(navController)
        }
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductRoute(productId!!)
        }
    }
}
