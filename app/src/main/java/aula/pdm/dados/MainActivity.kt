package aula.pdm.dados

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import aula.pdm.dados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt


class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var geradorRandomico: Random

    private lateinit var settingsActivityLauncher: ActivityResultLauncher<Intent>

    private lateinit var configuracoes: Configuracao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(activityMainBinding.root)

        geradorRandomico = Random(System.currentTimeMillis())

        val dado1: ImageView = activityMainBinding.resultadoIv
        val dado2: ImageView = activityMainBinding.resultado2Iv

        activityMainBinding.jogarDadosBt.setOnClickListener {

            val faces: Int = configuracoes.numeroFaces
            val dados: Int = configuracoes.numeroDados

            val resultado: Int = geradorRandomico.nextInt(1..faces)
            val resultado2: Int = geradorRandomico.nextInt(1..faces)

            val nomeImagem =  "dice_${resultado}"
            activityMainBinding.resultadoIv.setImageResource(
                resources.getIdentifier(nomeImagem, "mipmap", packageName)
            )

            val nomeImagem2 = "dice_${resultado2}"
            activityMainBinding.resultado2Iv.setImageResource(
                resources.getIdentifier(nomeImagem2, "mipmap", packageName)
            )

            if(faces < 7){
                if(dados == 1){
                    "A face sorteada foi $resultado".also { activityMainBinding.resultadoTv.text = it}

                    dado1.visibility = View.VISIBLE
                    dado2.visibility = View.GONE
                }
                else{
                    "A(s) face(s) sorteada(s) foi(oram) $resultado e $resultado2".also { activityMainBinding.resultadoTv.text = it}

                    dado1.visibility = View.VISIBLE
                    dado2.visibility = View.VISIBLE
                }
            }else{
                dado1.visibility = View.GONE
                dado2.visibility = View.GONE

                if(dados == 1)
                    "A face sorteada foi $resultado".also { activityMainBinding.resultadoTv.text = it}
                else
                    "A(s) face(s) sorteada(s) foi(oram) $resultado e $resultado2".also { activityMainBinding.resultadoTv.text = it}
            }
        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK){
                //Modificações na minha View
                if(result.data != null){
                    val configuracao: Configuracao? = result.data?.getParcelableExtra<Configuracao>(Intent.EXTRA_USER)

                    if (configuracao != null)
                        configuracoes = configuracao
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settingsMi){
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }

}