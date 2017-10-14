package br.com.alura.agenda.presenter;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.view.FormularioActivity;

/**
 * Created by Gabriel on 13/10/2017.
 * Presenter para manipular os dados da FormularioActivity
 */

public class FormularioHelper {
    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoTelefone;
    private EditText campoSite;
    private RatingBar campoNota;
    private Aluno aluno;

    public FormularioHelper(FormularioActivity activity){
        this.aluno = new Aluno();
        this.campoNome = (EditText) activity.findViewById(R.id.txtFormularioNome);
        this.campoEndereco = (EditText) activity.findViewById(R.id.txtFormularioEndereco);
        this.campoTelefone = (EditText) activity.findViewById(R.id.txtFormularioTelefone);
        this.campoSite = (EditText) activity.findViewById(R.id.txtFormularioSite);
        this.campoNota = (RatingBar) activity.findViewById(R.id.rattingFormularioNota);
    }

    public Aluno pegaAluno(){
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        return aluno;
    }

    public void preencheFormulario(Aluno aluno) {
        this.aluno = aluno;
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setRating(aluno.getNota().intValue());
    }
}
