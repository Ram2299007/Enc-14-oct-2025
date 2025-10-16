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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import eightbitlab.com.blurview.BlurView;
import com.Appzia.enclosure.Utils.WaveformView;

public class senderViewHolder extends RecyclerView.ViewHolder {
    public FloatingActionButton downlaodDoc;
    public ProgressBar progressBarDoc;
    public TextView downloadPercentageDocSender;
    public ImageButton pauseButtonDocSender;
    public RelativeLayout docDownloadControls;
    public TextView sendMessage, sendTime, docName, extension, cName, cPhone, firstText, forwarded, grpMsgName, readMore;
    public AppCompatImageView senderImg;

    public LinearLayout MainSenderBox, richBox, sendLinear;
    public LinearLayout docLyt, contactContainer, viewContact, llnew;


    public LinearLayout miceContainer, replydatalyt, cnamenamelyt, progresslyt, delete;
    public ImageView miceUImage;
    public AppCompatImageButton micePlay;
    public LinearProgressIndicator miceProgressbar;
    public TextView miceTiming, repliedData;
    public View replyDevider, viewbarlyt1;
    public LinearProgressIndicator viewnew;
    public ConstraintLayout menu2;
    public ImageView senderVideo;
    public RelativeLayout senderImgLyt;
    public FloatingActionButton downlaod;
    public ProgressBar progressBarImageview;
    public View blur;
    public RelativeLayout sendervideoLyt;
    public FloatingActionButton downlaodVideo;
    public ProgressBar progressBarVideo;
    public View blurVideo;
    public TextView captionText;
    public RelativeLayout richLinkViewLyt;
    public ImageView linkImg, linkImg2, videoicon;
    public TextView linkTitle;
    public TextView linkDesc;
    public TextView link, linkActualUrl;
    public LinearLayout datelyt;
    public TextView dateTxt;
    public TextView emojiText;
    public TextView docSizeExtension;
    public TextView docSize;
    public CardView emojiTextCard;
    public WaveformView waveformView;
    public ProgressBar progressBar;

    public FrameLayout frame;
    public CircularProgressIndicator circularProgressBar;
    public ImageView imageInsideProgressBar;
    public ImageView pdfPreview;
    public LinearLayout docFileIcon;
    public CardView pdfcard;

    public FrameLayout videoFrame;
    public TextView downloadPercentageImageSender;
    public ImageButton pauseButtonImageSender;
    public TextView downloadPercentageVideoSender;
    public ImageButton pauseButtonVideoSender;

    public RelativeLayout audioDownloadControls;
    public FloatingActionButton downlaodAudio;
    public ProgressBar progressBarAudio;
    public TextView downloadPercentageAudioSender;
    public ImageButton pauseButtonAudioSender;
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
    // Multi-selection checkbox
    public ImageView selectionCheckbox;
    public RelativeLayout senderImgBunchLyt;
    public ShapeableImageView img1;
    public ShapeableImageView img3;
    public ShapeableImageView img2;
    public ShapeableImageView img4;
    public FrameLayout img4Lyt;
    public TextView overlayTextImg;
    public FloatingActionButton downlaodImgBunch;
    public TextView downloadPercentageImageSenderBunch;



    public senderViewHolder(@NonNull View itemView) {
        super(itemView);
        sendMessage = itemView.findViewById(R.id.sendMessage);
        sendTime = itemView.findViewById(R.id.sendTime);
        docName = itemView.findViewById(R.id.docName);
        senderImg = itemView.findViewById(R.id.senderImg);
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
        senderVideo = itemView.findViewById(R.id.senderVideo);
        sendervideoLyt = itemView.findViewById(R.id.sendervideoLyt);


        senderImgLyt = itemView.findViewById(R.id.senderImgLyt);
        downlaod = itemView.findViewById(R.id.downlaod);
        progressBarImageview = itemView.findViewById(R.id.progressBar);
        blur = itemView.findViewById(R.id.blur);


        downlaodVideo = itemView.findViewById(R.id.downlaodVideo);
        progressBarVideo = itemView.findViewById(R.id.progressBarVideo);
        blurVideo = itemView.findViewById(R.id.blurVideo);
        captionText = itemView.findViewById(R.id.captionText);


        richLinkViewLyt = itemView.findViewById(R.id.richLinkViewLyt);
        linkImg = itemView.findViewById(R.id.linkImg);
        linkImg2 = itemView.findViewById(R.id.linkImg2);
        linkTitle = itemView.findViewById(R.id.linkTitle);
        linkDesc = itemView.findViewById(R.id.linkDesc);
        link = itemView.findViewById(R.id.link);
        linkActualUrl = itemView.findViewById(R.id.linkActualUrl);
        readMore = itemView.findViewById(R.id.readMore);

        datelyt = itemView.findViewById(R.id.datelyt);
        dateTxt = itemView.findViewById(R.id.dateTxt);
        MainSenderBox = itemView.findViewById(R.id.MainSenderBox);
        richBox = itemView.findViewById(R.id.richBox);
        emojiText = itemView.findViewById(R.id.emojiText);
        emojiTextCard = itemView.findViewById(R.id.emojiTextCard);
        videoFrame = itemView.findViewById(R.id.videoFrame);
        pdfPreview = itemView.findViewById(R.id.pdfPreview);
        pdfcard = itemView.findViewById(R.id.pdfcard);
        docFileIcon = itemView.findViewById(R.id.docFileIcon);
        docSizeExtension = itemView.findViewById(R.id.docSizeExtension);
        docSize = itemView.findViewById(R.id.docSize);
        videoicon = itemView.findViewById(R.id.videoicon);
        progressBar = itemView.findViewById(R.id.progressBar);
        downloadPercentageImageSender = itemView.findViewById(R.id.downloadPercentageImageSender);
        pauseButtonImageSender = itemView.findViewById(R.id.pauseButtonImageSender);

        downloadPercentageVideoSender = itemView.findViewById(R.id.downloadPercentageVideoSender);
        pauseButtonVideoSender = itemView.findViewById(R.id.pauseButtonVideoSender);

        downlaodDoc = itemView.findViewById(R.id.downlaodDoc);
        progressBarDoc = itemView.findViewById(R.id.progressBarDoc);
        downloadPercentageDocSender = itemView.findViewById(R.id.downloadPercentageDocSender);
        pauseButtonDocSender = itemView.findViewById(R.id.pauseButtonDocSender);
        docDownloadControls = itemView.findViewById(R.id.docDownloadControls);

        audioDownloadControls = itemView.findViewById(R.id.audioDownloadControls);
        downlaodAudio = itemView.findViewById(R.id.downlaodAudio);
        progressBarAudio = itemView.findViewById(R.id.progressBarAudio);
        downloadPercentageAudioSender = itemView.findViewById(R.id.downloadPercentageAudioSender);
        pauseButtonAudioSender = itemView.findViewById(R.id.pauseButtonAudioSender);

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

        // Initialize multi-selection checkbox
        selectionCheckbox = itemView.findViewById(R.id.selectionCheckbox);
        // Initialize sendLinear for multi-selection
        sendLinear = itemView.findViewById(R.id.sendLinear);
        
        // Visual indicators removed - system is working correctly
        senderImgBunchLyt = itemView.findViewById(R.id.senderImgBunchLyt);
        img1 = itemView.findViewById(R.id.img1);
        img2 = itemView.findViewById(R.id.img2);
        img3 = itemView.findViewById(R.id.img3);
        img4 = itemView.findViewById(R.id.img4);
        img4Lyt = itemView.findViewById(R.id.img4Lyt);
        overlayTextImg = itemView.findViewById(R.id.overlayTextImg);
        downlaodImgBunch = itemView.findViewById(R.id.downlaodImgBunch);
        downloadPercentageImageSenderBunch = itemView.findViewById(R.id.downloadPercentageImageSenderBunch);
    }
}