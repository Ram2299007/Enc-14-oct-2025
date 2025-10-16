package com.Appzia.enclosure.Utils.ChatadapterFiles;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import eightbitlab.com.blurview.BlurView;

public class receiverViewHolder extends RecyclerView.ViewHolder {
    public TextView downloadPercentageImage;
    public TextView recMessage, recTime, docName, extension, cName, cPhone, firstText, grpMsgName, readMore;
    public AppCompatImageView recImg;
    public BlurView blurView;
    public LinearLayout docLyt, contactContainer, viewContact, recLinear;
    public ConstraintLayout menu2;
    public RelativeLayout receivervideoLyt;
    public ImageView selectionCheckbox;
    
    // Additional UI elements from sample_receiver.xml
    public TextView startData;
    public ImageView camerapic, replysvg;
    public LinearLayout lyt;
    public CardView date;
    public View cancel;

    public LinearLayout miceContainer, replydatalyt, llnew, cnamenamelyt, progresslyt, delete;
    public ImageView miceUImage;
    public AppCompatImageButton micePlay;
    public LinearProgressIndicator miceProgressbar;
    public TextView miceTiming, repliedData, forwarded;
    public View replyDevider, viewbarlyt1;
    public LinearProgressIndicator viewnew;
    public ImageView recVideo, videoicon;

    public RelativeLayout receiverImgLyt;
    public FloatingActionButton downlaod;
    public ProgressBar progressBarImageview;
    public View blur;

    public FloatingActionButton downlaodVideo;
    public ProgressBar progressBarVideo;
    public View blurVideo;
    public TextView captionText;
    public RelativeLayout richLinkViewLyt;


    public ImageView linkImg, linkImg2;
    public TextView linkTitle;
    public TextView linkDesc;
    public TextView link, linkActualUrl;
    public LinearLayout datelyt, MainReceiverBox;
    public TextView dateTxt;
    public TextView emojiText;
    public CardView emojiTextCard, cardview;
    public FrameLayout videoFrame;

    public TextView docSizeExtension;
    public TextView docSize, originalNumber, originalName;
    public ImageView pdfPreview;
    public LinearLayout docFileIcon;
    public CardView pdfcard;
    public LinearLayoutCompat originalDelete, originalAdd;
    public ImageView personaddTheme;
    public ImageButton pauseButtonImage;
    public TextView downloadPercentageVideo;
    public ImageButton pauseButtonVideo;
    // Receiver doc download controls (left-side)
    public RelativeLayout docDownloadControlsReceiver;
    public FloatingActionButton downlaodDocReceiver;
    public ProgressBar progressBarDocReceiver;
    public TextView downloadPercentageDocReceiver;
    public ImageButton pauseButtonDocReceiver;
    // Receiver voice-audio download controls (left)
    public RelativeLayout audioDownloadControlsReceiver;
    public FloatingActionButton downlaodAudioReceiver;
    public ProgressBar progressBarAudioReceiver;
    public TextView downloadPercentageAudioReceiver;
    public ImageButton pauseButtonAudioReceiver;


    public LinearLayout replylyoutGlobal;
    public LinearLayout pageLyt;
    public TextView msgreplyText;
    public TextView pageText;
    public LinearLayout contactContainerReply;
    public TextView replyYou;
    public TextView firstTextReply;
    public LinearLayout replyTheme;
    public CardView imgcardview;
    public CardView musicReply;
    public CardView miceReply;
    public ImageView imgreply;

    public RelativeLayout recImgBunchLyt;
    public ShapeableImageView img1;
    public ShapeableImageView img3;
    public ShapeableImageView img2;
    public ShapeableImageView img4;
    public FrameLayout img4Lyt;
    public TextView overlayTextImg;
    public FloatingActionButton downlaodImgBunch;
    public TextView downloadPercentageImageSenderBunch;
    public ImageButton pauseButtonImageSenderBunch;
    public ProgressBar progressBarImgBunch;
    public com.Appzia.enclosure.Utils.MediaBunchLayout mediaBunchLayout;

    public receiverViewHolder(@NonNull View itemView) {
        super(itemView);
        recMessage = itemView.findViewById(R.id.recMessage);
        recTime = itemView.findViewById(R.id.recTime);
        docName = itemView.findViewById(R.id.docName);
        recImg = itemView.findViewById(R.id.recImg);
        docLyt = itemView.findViewById(R.id.docLyt);
        extension = itemView.findViewById(R.id.extension);
        contactContainer = itemView.findViewById(R.id.contactContainer);
        cName = itemView.findViewById(R.id.cName);
        cPhone = itemView.findViewById(R.id.cPhone);
        viewContact = itemView.findViewById(R.id.viewContact);
        firstText = itemView.findViewById(R.id.firstText);
        miceContainer = itemView.findViewById(R.id.miceContainer);
        miceUImage = itemView.findViewById(R.id.miceUImage);
        micePlay = itemView.findViewById(R.id.micePlay);
        miceProgressbar = itemView.findViewById(R.id.miceProgressbar);
        miceTiming = itemView.findViewById(R.id.miceTiming);
        replyDevider = itemView.findViewById(R.id.replyDevider);
        repliedData = itemView.findViewById(R.id.repliedData);
        replydatalyt = itemView.findViewById(R.id.replydatalyt);
        viewbarlyt1 = itemView.findViewById(R.id.viewbarlyt1);
        llnew = itemView.findViewById(R.id.llnew);
        viewnew = itemView.findViewById(R.id.viewnew);
        cnamenamelyt = itemView.findViewById(R.id.cnamenamelyt);
        progresslyt = itemView.findViewById(R.id.progresslyt);
        menu2 = itemView.findViewById(R.id.menu2);
        delete = itemView.findViewById(R.id.delete);
        forwarded = itemView.findViewById(R.id.forwarded);
        grpMsgName = itemView.findViewById(R.id.grpMsgName);
        receivervideoLyt = itemView.findViewById(R.id.receivervideoLyt);
        recVideo = itemView.findViewById(R.id.recVideo);
        receiverImgLyt = itemView.findViewById(R.id.receiverImgLyt);
        downlaod = itemView.findViewById(R.id.downlaod);
        progressBarImageview = itemView.findViewById(R.id.progressBar);
        blur = itemView.findViewById(R.id.blur);
        downlaodVideo = itemView.findViewById(R.id.downlaodVideo);
        progressBarVideo = itemView.findViewById(R.id.progressBarVideo);
        blurVideo = itemView.findViewById(R.id.blurVideo);
        captionText = itemView.findViewById(R.id.captionText);
        readMore = itemView.findViewById(R.id.readMore);
        richLinkViewLyt = itemView.findViewById(R.id.richLinkViewLyt);
        linkImg = itemView.findViewById(R.id.linkImg);
        linkImg2 = itemView.findViewById(R.id.linkImg2);
        linkTitle = itemView.findViewById(R.id.linkTitle);
        linkDesc = itemView.findViewById(R.id.linkDesc);
        link = itemView.findViewById(R.id.link);
        linkActualUrl = itemView.findViewById(R.id.linkActualUrl);
        datelyt = itemView.findViewById(R.id.datelyt);
        dateTxt = itemView.findViewById(R.id.dateTxt);
        MainReceiverBox = itemView.findViewById(R.id.MainReceiverBox);
        emojiText = itemView.findViewById(R.id.emojiText);
        emojiTextCard = itemView.findViewById(R.id.emojiTextCard);
        videoFrame = itemView.findViewById(R.id.videoFrame);
        pdfPreview = itemView.findViewById(R.id.pdfPreview);
        pdfcard = itemView.findViewById(R.id.pdfcard);
        docFileIcon = itemView.findViewById(R.id.docFileIcon);
        docSizeExtension = itemView.findViewById(R.id.docSizeExtension);
        docSize = itemView.findViewById(R.id.docSize);
        videoicon = itemView.findViewById(R.id.videoicon);
        cardview = itemView.findViewById(R.id.cardview);
        originalNumber = itemView.findViewById(R.id.originalNumber);
        originalName = itemView.findViewById(R.id.originalName);
        originalDelete = itemView.findViewById(R.id.originalDelete);
        originalAdd = itemView.findViewById(R.id.originalAdd);
        personaddTheme = itemView.findViewById(R.id.personaddTheme);
        downloadPercentageImage = itemView.findViewById(R.id.downloadPercentageImage);
        pauseButtonImage = itemView.findViewById(R.id.pauseButtonImage);
        downloadPercentageVideo = itemView.findViewById(R.id.downloadPercentageVideo);
        pauseButtonVideo = itemView.findViewById(R.id.pauseButtonVideo);
        docDownloadControlsReceiver = itemView.findViewById(R.id.docDownloadControlsReceiver);
        downlaodDocReceiver = itemView.findViewById(R.id.downlaodDocReceiver);
        progressBarDocReceiver = itemView.findViewById(R.id.progressBarDocReceiver);
        downloadPercentageDocReceiver = itemView.findViewById(R.id.downloadPercentageDocReceiver);
        pauseButtonDocReceiver = itemView.findViewById(R.id.pauseButtonDocReceiver);

        audioDownloadControlsReceiver = itemView.findViewById(R.id.audioDownloadControlsReceiver);
        downlaodAudioReceiver = itemView.findViewById(R.id.downlaodAudioReceiver);
        progressBarAudioReceiver = itemView.findViewById(R.id.progressBarAudioReceiver);
        downloadPercentageAudioReceiver = itemView.findViewById(R.id.downloadPercentageAudioReceiver);
        pauseButtonAudioReceiver = itemView.findViewById(R.id.pauseButtonAudioReceiver);


        // New reply design
        replylyoutGlobal = itemView.findViewById(R.id.replylyoutGlobal);
        replyYou = itemView.findViewById(R.id.replyYou);
        msgreplyText = itemView.findViewById(R.id.msgreplyText);
        replyTheme = itemView.findViewById(R.id.replyTheme);
        imgcardview = itemView.findViewById(R.id.imgcardview);
        imgreply = itemView.findViewById(R.id.imgreply);
        contactContainerReply = itemView.findViewById(R.id.contactContainerReply);
        firstTextReply = itemView.findViewById(R.id.firstTextReply);
        pageLyt = itemView.findViewById(R.id.pageLyt);
        pageText = itemView.findViewById(R.id.pageText);
        musicReply = itemView.findViewById(R.id.muciReply);
        miceReply = itemView.findViewById(R.id.miceReply);

        // Initialize recLinear for multi-selection
        recLinear = itemView.findViewById(R.id.recLinear);

        // Initialize selectionCheckbox for multi-selection
        selectionCheckbox = itemView.findViewById(R.id.selectionCheckbox);


        recImgBunchLyt = itemView.findViewById(R.id.recImgBunchLyt);
        img1 = itemView.findViewById(R.id.img1);
        img2 = itemView.findViewById(R.id.img2);
        img3 = itemView.findViewById(R.id.img3);
        img4 = itemView.findViewById(R.id.img4);
        img4Lyt = itemView.findViewById(R.id.img4Lyt);
        overlayTextImg = itemView.findViewById(R.id.overlayTextImg);
        downlaodImgBunch = itemView.findViewById(R.id.downlaodImgBunch);
        downloadPercentageImageSenderBunch = itemView.findViewById(R.id.downloadPercentageImageSenderBunch);
        pauseButtonImageSenderBunch = itemView.findViewById(R.id.pauseButtonImageSenderBunch);
        progressBarImgBunch = itemView.findViewById(R.id.progressBarImgBunch);
        mediaBunchLayout = itemView.findViewById(R.id.mediaBunchLayout);

        // Additional UI elements from sample_receiver.xml
        startData = itemView.findViewById(R.id.startData);
        camerapic = itemView.findViewById(R.id.camerapic);
        replysvg = itemView.findViewById(R.id.replysvg);
        lyt = itemView.findViewById(R.id.lyt);
        date = itemView.findViewById(R.id.date);
        cancel = itemView.findViewById(R.id.cancel);
        
        // Ensure MainReceiverBox has the correct background
        if (MainReceiverBox != null) {
            MainReceiverBox.setBackgroundResource(com.Appzia.enclosure.R.drawable.message_bg_gray);
        }
    }
    
    /**
     * Ensure MainReceiverBox has the correct background
     * This method should be called after any styling changes
     */
    public void ensureCorrectBackground() {
        if (MainReceiverBox != null) {
            MainReceiverBox.setBackgroundResource(com.Appzia.enclosure.R.drawable.message_bg_gray);
        }
    }

}