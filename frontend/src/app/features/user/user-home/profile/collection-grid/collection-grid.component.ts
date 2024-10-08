import { Component, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PostCollection } from 'src/app/core/interfaces/post-collection';
import { ConstructImageUrlPipe } from 'src/app/core/pipes/construct-image-url.pipe';
import { PostCollectionService } from 'src/app/core/services/post-collection.service';
import { ToastService } from 'src/app/core/services/toast.service';
import { ImageUtils } from 'src/app/core/utils/image-utils';

@Component({
  selector: 'app-collection-grid',
  templateUrl: './collection-grid.component.html',
  styleUrls: ['./collection-grid.component.scss'],
  providers: [ConstructImageUrlPipe]
})

export class CollectionGridComponent {
  @Input() userProfileId: string = ''; // the owner of the collections
  @Input() currentUserId: string = ''; // the current user id

  collections: PostCollection[] = []; // the collections to show
  selectedCollection: PostCollection = {}; // the selected collection to show in dialog
  isVisibleAddCollection: boolean = false; // show or hide the dialog
  
  selectedImage: any; // the selected image to upload
  selectedImageDataUrl: any; // the selected image data url

  constructor(
    private postCollectionService: PostCollectionService,
    private toastService: ToastService,
    private translateService: TranslateService,
    private constructImageUrlPipe: ConstructImageUrlPipe,
    public imageUtils: ImageUtils
  ) { }


  ngOnChanges(changes: any) {
    // Reset collections when the user profile changes.
    if (changes.userProfileId && changes.userProfileId.currentValue) {
      this.getCollections();
    }
  }


  getCollections() {
    this.postCollectionService.getByCreatedById(this.userProfileId).subscribe({
      next: (data) => {
        this.collections = data;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }


  onSelectImage(event: any) {
    const file = event.files[0];
    if (file) {
      this.selectedImage = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedImageDataUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }


  newCollection() {
    this.reset();
    this.isVisibleAddCollection = true;
  }


  editCollection(collection: PostCollection) {
    this.reset();
    
    let imageUrl = this.constructImageUrlPipe.transform(collection.imageUrl);
    // check if the image exists
    var request = new XMLHttpRequest();
    request.open("GET", imageUrl, true);
    request.send();
    request.onload = () => {
      if (request.status == 200) {
        // image exists
        this.selectedImageDataUrl = imageUrl;
        this.selectedCollection = { ...collection };
      } else {
        // image does not exist
        this.selectedCollection = { ...collection };
      }
    }
    this.isVisibleAddCollection = true;
  }


  saveCollection() {
    const formData = new FormData();
    formData.append('postCollection', new Blob(
      [JSON.stringify(this.selectedCollection)], 
      { type: 'application/json' }
    ));

    if (this.selectedCollection.id) {
      if(this.selectedImage) {
        formData.append('photo', new File([this.selectedImage], this.selectedCollection.id + '.jpg', {
          type: this.selectedImage.type,
          lastModified: this.selectedImage.lastModified,
        }));
      }

      // Update
      this.postCollectionService.update(formData).subscribe({
        next: (data) => {
          this.isVisibleAddCollection = false;
          this.toastService.showSuccess(
            this.translateService.instant('common.success'), 
            this.translateService.instant('collection_grid.update_collection_successfully')
          );
          this.imageUtils.refreshDateTime();
          // reset
          this.reset();
        },
        error: (error) => {
          console.error(error);
        }
      });
    } 
    else {
      if(this.selectedImage) {
        formData.append('photo', this.selectedImage);
      }

      // Create
      this.postCollectionService.create(formData).subscribe({
        next: (data) => {
          this.isVisibleAddCollection = false;
          this.toastService.showSuccess(
            this.translateService.instant('common.success'), 
            this.translateService.instant('collection_grid.collection_created_successfully')
          );
          // set sticker group id and add to table
          this.selectedCollection.id = data;
          this.selectedCollection.imageUrl = "collections/" + data + ".jpg";
          this.collections = [this.selectedCollection, ...(this.collections || [])];
          // reset
          this.reset();
        },
        error: (error) => {
          console.error(error);
          this.toastService.showError(this.translateService.instant('common.error'), error.error.message);
        }
      });
    }
  }


  deleteImage() {
    if(!this.selectedCollection.id) return;

    this.postCollectionService.deleteImage(this.selectedCollection.id).subscribe({
      next: (data) => {
        if(data) {
          this.toastService.showSuccess(
            this.translateService.instant('common.success'), 
            this.translateService.instant('collection_grid.collection_image_deleted_successfully')
          );
          this.selectedImageDataUrl = null;
        }
      },
      error: (error) => {
        console.error(error);
        this.toastService.showError(this.translateService.instant('common.error'), error.error.message);
      }
    });
  }


  reset() {
    this.selectedCollection = {};
    this.selectedImage = null;
    this.selectedImageDataUrl = null;
    this.isVisibleAddCollection = false;
  }

}
