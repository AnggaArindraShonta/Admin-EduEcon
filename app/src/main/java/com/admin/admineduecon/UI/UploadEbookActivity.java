package com.admin.admineduecon.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.admin.admineduecon.Model.Ebook;
import com.admin.admineduecon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class UploadEbookActivity extends AppCompatActivity {

    private CardView addPdf;
    private EditText pdfTitle;
    private ImageButton uploadPdfBtn;
    private TextView tvPdf;
    private String pdfName, title;

    private final  int REQ = 1;
    private Uri pdfData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ebook);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addPdf = findViewById(R.id.addPdf);
        pdfTitle = findViewById(R.id.judulPdf);
        uploadPdfBtn = findViewById(R.id.uploadPdfBtn);
        tvPdf = findViewById(R.id.tvPdf);

        addPdf.setOnClickListener(v -> openGallery());

        uploadPdfBtn.setOnClickListener(view -> {
            title = pdfTitle.getText().toString();
            if (title.isEmpty()){
                pdfTitle.setError("empty");
                pdfTitle.requestFocus();
            }else if (pdfData == null){
                Toast.makeText(UploadEbookActivity.this, "Please Upload PDF", Toast.LENGTH_SHORT).show();
            }else {
                uploadPdf();
            }
        });
    }

    private void uploadPdf() {
        pd.setTitle("Please wait...");
        pd.setTitle("Uploading pdf");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask =  taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri uri = uriTask.getResult();
                    uploadData(String.valueOf(uri));
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadEbookActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String dowloadUrl) {
        databaseReference = databaseReference.child("pdf");
        final String id = databaseReference.push().getKey();

        String title =  pdfTitle.getText().toString();

        Ebook ebook = new Ebook(id, title, dowloadUrl);

        databaseReference.child(id).setValue(ebook).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadEbookActivity.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UploadEbookActivity.this, EbookActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadEbookActivity.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
                pdfTitle.setText("");
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf file"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == REQ && resultCode == RESULT_OK);{
            pdfData = data.getData();

            if (pdfData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = UploadEbookActivity.this.getContentResolver().query(pdfData,null,null,null,null);
                    if (cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
            }
            tvPdf.setText(pdfName);
        }
    }
}