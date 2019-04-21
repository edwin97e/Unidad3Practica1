package mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText nombre, descripcion, cantidad, fecha;
        final Button insertar, eliminar, actualizar, consultar;
        ListView lista;
        List<mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto> datosConsultarProducto;

        DatabaseReference servicioRealtime;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nombre = findViewById(R.id.nombre);
        descripcion = findViewById( R.id.descripcion);
        cantidad = findViewById(R.id.cantidad);
        fecha = findViewById(R.id.fecha);

        insertar = findViewById(R.id.insertar);
        eliminar = findViewById(R.id.eliminar);
        actualizar = findViewById(R.id.actualizar);
        consultar = findViewById(R.id.consultar);

        servicioRealtime = FirebaseDatabase.getInstance().getReference();

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarProducto();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actializarProducto();
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarProducto();
            }
        });
    }

    private void insertarProducto(){
        mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto producto = new mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto(nombre.getText().toString(), descripcion.getText().toString(), cantidad.getText().toString(), fecha.getText().toString());
        servicioRealtime.child("productos").push().setValue(producto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main2Activity.this, "Insertado con éxito!", Toast.LENGTH_SHORT).show();
                        nombre.setText("");
                        descripcion.setText("");
                        cantidad.setText("");
                        fecha.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this, "Error al insertar!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarProducto(){
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        final EditText id = new EditText(this);
        id.setHint("Id a eliminar");
        a.setTitle("Eliminar")
                .setMessage("Ingrese el id a eliminar")
                .setView(id).setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarId(id.getText().toString());
            }
        })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminar(String s){
        servicioRealtime.child("marcas").child(s).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main2Activity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this, "Error al eliminar!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void actualizarProducto(){
        Map<String, Object> map = new HashMap<>();
        mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto producto = new mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto(nombre.getText().toString(), descripcion.getText().toString(), cantidad.getText().toString(), fecha.getText().toString());
        map.put("productos", producto);

        servicioRealtime.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Main2Activity.this, "Bien", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void consultarProducto(){
        servicioRealtime.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosConsultaProducto = new ArrayList<>();

                servicioRealtime.child("productos").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap : dataSnapshot.getChildren()){
                            mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto producto = snap.getValue(mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto.class);

                            if(producto!=null){
                                datosConsultaProducto.add(producto);
                            }
                        }
                        crearListView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void crearListView(){
        if(datosConsultaProducto.size()<=0){
            return;
        }

        String[] nombres = new String[datosConsultaPoducto.size()];
        for(int i = 0; i<nombres.length;i++){
            mx.edu.edwinponceittepic.tpdm_u3_practica1_edwinponce.Producto j = datosConsultaProducto.get(i);
            nombres[i] = j.descripcion;

        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listaProductos.setAdapter(adaptador);
    }



}
