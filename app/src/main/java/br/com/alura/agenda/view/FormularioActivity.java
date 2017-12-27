package br.com.alura.agenda.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import br.com.alura.agenda.BuildConfig;
import br.com.alura.agenda.presenter.FormularioHelper;
import br.com.alura.agenda.R;
import br.com.alura.agenda.model.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 567;
    private FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        if(aluno != null){
            this.helper.preencheFormulario(aluno);
        }

        Button btCamera = (Button) findViewById(R.id.btCamera);
        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(FormularioActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider", new File(caminhoFoto)));
                //startActivity(intentCamera);

                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode == RESULT_OK){
            if(requestCode == CODIGO_CAMERA){
                helper.carregaImagem(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.pegaAluno();

                // Aqui instanciamos o DAO e inserimos o novo aluno no banco
                AlunoDAO dao = new AlunoDAO(this);

                if (aluno.getId() == null){
                    dao.insere(aluno);
                } else {
                    dao.altera(aluno);
                }

                dao.close();

                Toast.makeText(FormularioActivity.this,"Aluno " + aluno.getNome() + " salvo!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
