package com.example.tarefas_gestor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.tarefas_gestor.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var menu: Menu
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var idMenuAtual : Int = R.menu.menu_main
        set(value) {
            if (value != field) {
                field = value
                invalidateOptionsMenu()
            }
        }

    var fragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(idMenuAtual, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == R.id.action_settings) {
            return true
        }

        val opcaoProcessada = when (fragment) {
            is ListaTarefasFragment -> (fragment as ListaTarefasFragment).processaOpcaoMenu(item)
            is EditarTarefaFragment -> (fragment as EditarTarefaFragment).processaOpcaoMenu(item)
            is EliminarTarefaFragment -> (fragment as EliminarTarefaFragment).processaOpcaoMenu(item)
            is ListaCategoriaFragment -> (fragment as ListaCategoriaFragment).processaOptionMenu(item)
            is EditarCategoriaFragment -> (fragment as EditarCategoriaFragment).processaOpcaoMenu(item)
            else -> false
        }

        return if (opcaoProcessada) { true } else { super.onOptionsItemSelected(item) }
    }


override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
}

fun mostraOpcaoMenu(idOpcao: Int, mostrar: Boolean) {
    menu.findItem(idOpcao).setVisible(mostrar)
}
fun atualizaNome(label: String) = binding.toolbar.setTitle(label)

}