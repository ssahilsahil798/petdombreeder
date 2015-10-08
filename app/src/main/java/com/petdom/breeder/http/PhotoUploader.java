package com.petdom.breeder.http;

import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.modal.Photo;
import com.petdom.breeder.modal.PhotoList;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by diwakar.mishra on 9/30/2015.
 */
public class PhotoUploader implements Runnable {

    private static final String TAG = PhotoUploader.class.getSimpleName();
    private LinkedList<Photo> queue;
    private Thread thread;
    private static PhotoUploader instance=new PhotoUploader();

    private PhotoUploader() {
        this.queue = new LinkedList<>();
    }

    public static PhotoUploader getInstance() {
        return instance;
    }

    protected void queue(Photo photo){
        if (!queue.contains(photo)){
            queue.add(photo);
        }
    }

    protected void startIfRequired() {
        if (thread==null || !thread.isAlive()){
            thread  = new Thread(this,TAG);
            thread.start();
        }
    }

    @Override
    public void run() {
        while (!queue.isEmpty()){
            process(queue.poll());
        }
    }

    private void process(Photo photo) {

        String url=URLConstants.URL_UPLOAD_CERTIFICATES + "?" + URLConstants.URL_DEFAULT_PARAMS_V1 + "&breeder=" + photo.getBreederId()+ "&dog=" + photo.getDogId()+ "&type=" + photo.getKey();
        try {
            onUploadStatusChanged(photo,Photo.STATUS_UPLOADING);

            Response response = HttpUtil.upload(url, photo);
            int code=response.code();
            if (code!=201){
                throw new InvalidResponseCodeException(code);
            }
            onUploadStatusChanged(photo,Photo.STATUS_UPLOADED);
        } catch (Exception e) {
            onUploadStatusChanged(photo,Photo.STATUS_FAILED);
        }
    }

    private void onUploadStatusChanged(Photo photo, int status) {
        if (status==Photo.STATUS_UPLOADED){
            DataController.getInstance().deletePhoto(photo);
        }else{
            photo.setStatus(status);
            DataController.getInstance().commitPhotos();
        }
    }

    public void prepare() {
        PhotoList photoList = DataController.getInstance().getPhotoList();
        ArrayList<Photo> itemToRemove=new ArrayList<>();
        for (int i=0;i<photoList.size();i++){
            Photo p = photoList.get(i);
            if (p.getStatus()==Photo.STATUS_FAILED || p.getStatus()==Photo.STATUS_NONE){
                queue(p);
            }else if(p.getStatus()==Photo.STATUS_UPLOADED){
                itemToRemove.add(p);
            }
        }

        startIfRequired();

        for (int i=0;i<itemToRemove.size();i++){
            Photo p = itemToRemove.get(i);
            photoList.remove(p);
        }
        if (itemToRemove.size()>0){
            DataController.getInstance().commitPhotos();
        }
    }
}
