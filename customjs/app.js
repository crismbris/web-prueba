
var globalUser = JSON.parse(localStorage.getItem('currentUser'));

$(document).ready(function () {
    SetUser();

    //GetIndexInfo();
    GetSingleIdea();
    GetIdeas(-1);
    //PostIdea();

    ApplyFilter();

});

function SetUser() {
    $('#navbarDropdown').text(globalUser.username);
}

function ApplyFilter() {
    $('#filter-category').on('change', function (e) {
        var optionSelected = $("option:selected", this);
        var valueSelected = this.value;
        console.log('>>> valueSelected:' + valueSelected);

        console.log('>>> applying filter');
        $('.dynamic-row').remove();
        GetIdeas(valueSelected);
    });
}


function GetIndexInfo() {
    $.getJSON("https://jsonplaceholder.typicode.com/todos/1", function (data) {
        $.each(data, function (key, val) {
            console.log("key: " + key + ", val: " + val);
        });
    });

}

function GetSingleIdea(id) {
    $.getJSON("http://localhost:8080/ideas/"+id, function (data) {
       var singleID = data["title"];
       var singleDescription= data["description"];
       var singleSaleMode=data["saleMode"];
       var singlePrice=data["price"];
       var singleAuthor = data["createdBy"];
       var result = [singleID, singleDescription, singleSaleMode, singlePrice, singleAuthor];
    });
    
}

function GetIdeas(categoryFilter) {
var categories = ["No Category","Technology", "Sport", "Fashion", "Agriculture", "Wildlife", "History", "Geography",
     "Mathematics", "Language", "Physics", "Information Technology"];

    $.getJSON("http://localhost:8080/ideas/", function (data) {
        $.each(data,function (key,value){
            var title = data[key].title;
            var description = data[key].description;
            var ideaid = data[key].id
             $("#idea-title").text(data["title"]);

            var markupRow = `
            <tr class='dynamic-row'>
                
                <td id ='table-ideas-title'>` + title + `</td>
                <td id ='table-ideas-description'>` + description + `</td>
                <td id='td-see-more'><button type='button' class='btn btn-secondary btn-lg' data-toggle='modal' data-target='#myModal` + ideaid + `'>See more</button></td>
            </tr>
            `;
           $.getJSON("http://localhost:8080/ideas/"+ideaid, function (data2) {
            var singleSaleMode=data2["saleMode"];
            var singlePrice=data2["price"];
            var singleAuthor = data2["createdBy"];
            var votes = data2["votes"];
            var numCategory = data2["idCategories"];

            var markupModal = `
            <div class="modal fade dynamic-row" id="myModal` + ideaid + `" role="dialog">
                <div class="modal-dialog modal-lg" >
                
                <div class="modal-content">
                    <div class="modal-header">
                    <h5>` + title + `</h5>
                    <button type="button" class="close" data-dismiss="modal" style="margin-left: 0;">&times;</button>
                    </div>
                    <div class="modal-body">
                    <div class="container">
                        <div class="row">
                            <div class="col-4">
                                <h6>Author</h6>
                                <div class="single-author">`+ (singleAuthor == null?"Anonymous":singleAuthor) +`</div>
                                <h6>Category</h6>
                                <div class="single-author">`+categories[(numCategory.length == 0)? 0 :numCategory[0]]+`</div>
                                <h6>Price</h6>
                                <div class="single-author">`+ (singlePrice == null?"No price":singlePrice)+`</div>
                                <h6>Vote!</h6>
                                <button type="button" id="btn-like-` + ideaid + `" class="btn btn-outline-success ` + (votes[0] == 1? `active` : ``) + `" onclick="votePosIdea(`+ideaid+`);">I like it</button>
                                <button type="button" id="btn-dislike-` + ideaid + `" class="btn btn-outline-danger ` + (votes[1] == 1? `active` : ``) + `" onclick="voteNegIdea(`+ideaid+`);">I don't like it</button>
                                <h6></h6>
                                <h6> Likes: <span id="modal-likes-` +ideaid+ `">`+ votes[0]+`</span></h6>
                                <h6> Dislikes: <span id="modal-dislikes-` +ideaid+ `">`+ votes[1]+`</span></h6>
                            </div>
                            <div class="col-8">
                                <div class = "description">`+description+`</div>
                            </div>
    
                        </div>
                    </div>
                    </div>

                    <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
                
                </div>
            </div>
            `;

            if (categoryFilter == -1 || categoryFilter==numCategory[0] ) {
                $("table tbody").append(markupRow);
                $("table tbody").append(markupModal);
            }
        });
        });
    });
}

function PostIdea(ideaData) {
    /*var ideaData = {
        title: 'fooooooooooooooo',
        body: 'barrrrrrrrrrrrrrr',
        userId: 1,
    };*/

    $.ajax({
        type: "POST",
        data: JSON.stringify(ideaData),
        //url: "https://jsonplaceholder.typicode.com/posts",
        url: "http://localhost:8080/ideas/add",
        contentType: 'application/json; charset=utf-8',
    }).done(function (response) {
        console.log("Done! " + response.id + " - " + response.title);
        alert("Idea created!")
    }).fail(showError);

}

function showError(jqXHR) {
        console.log(jqXHR.status);
        console.log(jqXHR.statusText);
    }


  function addIdea() {
    var cat = $("#category1").val();
    alert(cat);
   var ideaData = 
    {
        "title":$("#title1").val(),
        "description":  $("#description1").val(),
        "saleMode":$("#sell1").val(),
        "price":$("#price1").val(),
        "state": "A",
        "created": null,
        "updated": null,
        "createdBy": null,
        "updatedBy": null,
        "ideaCategoryCollection": [{"categoryId":+cat}]
    };
    /************VALIDACIONES DE LOS CAMPOS DEL FORMULARIO *****************/
    //Titlo de no mas de 100 caracteres.
    title = document.getElementById("title1").value;
    if( title == null || title.length == 0 ) {
        alert("The field title is empty. Please fill this field.")
        return false;
    }
    if(title.length >100){
        alert("The field 'title' should not have more than 100 characters.")
        return false;
    }

    //description no mas de 5000 caracteres
    description = document.getElementById("description1").value;
    if( description == null || description.length == 0 ) {
        alert("The field description is empty. Please fill this field.")
        return false;
    }
    if(description.length >5000){
        alert("The field 'description' should not have more than 5000 characters.")
        return false;
    }

    //chekeo si ha elegido una opción de categoria
    indice = document.getElementById("category1").selectedIndex;
    if( indice == null || indice == 0 ) {
        alert("You should select a category option.")
        return false;
    }

    //chekeo que se ha elegido un modo de venta
    indice = document.getElementById("sell1").selectedIndex;
    if( indice == null || indice == 0 ) {
        alert("You should select a sell option.")
        return false;
    }
    //ahora compruebo que se indice ==1, entonces es venta:
    //Por lo tanto el campo rpice debe ser relleno con un valor numerico
    if(indice == 1){
       //Es una venta
       //Compruebo que el campo este relleno y sea un valor numerico
       valor = $("#price1").val();
       if( isNaN(valor)   ) {
         alert("The field price should have a numeric value");
         return false;
        }
        if(valor == null || valor.length == 0){
            alert("The field PRICE is empty. Please, fill this field with a numeric value.");
            return false;
        }
    }

   PostIdea(ideaData);
}

function votePosIdea(ideaid){
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/ideas/"+ideaid+"/vote/1",
        data: "",
        contentType: 'application/json',
        dataType: 'json',
    }).done(function (xxx) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/ideas/"+ideaid,
            data: "",
            contentType: 'application/json',
            dataType: 'json',
        }).done(function (retrievedIdea) {
            setLikes(ideaid, retrievedIdea);
            setDis(ideaid, retrievedIdea);
            //alert("Liked! " + retrievedIdea.votes[0] + " - " + retrievedIdea.votes[1]);
        }).fail(showError);
    });
}

function voteNegIdea(ideaid){
    $.ajax({
        type: "POST",
        data: "",
        url: "http://localhost:8080/ideas/"+ideaid+"/vote/-1",
        contentType: 'application/json',
        dataType: 'json',
    }).done(function (xxx) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/ideas/"+ideaid,
            data: "",
            contentType: 'application/json',
            dataType: 'json',
        }).done(function (retrievedIdea) {
            setLikes(ideaid, retrievedIdea);
            setDis(ideaid, retrievedIdea);
            //alert("Disliked! " + retrievedIdea.votes[0] + " - " + retrievedIdea.votes[1]);
        }).fail(showError);
    });
}

function setLikes(ideaid, setOfVotes) {
    var likesSpan = '#modal-likes-' + ideaid;
    $(likesSpan).text(setOfVotes.votes[0]);

    var likeBtn = '#btn-like-' + ideaid;
    if (setOfVotes.votes[0] == 0) {
        $(likeBtn).removeClass('active');
    } else {
        $(likeBtn).addClass('active');
    }
}

function setDis(ideaid, setOfVotes) {
    var dislikesSpan = '#modal-dislikes-' + ideaid;
    $(dislikesSpan).text(setOfVotes.votes[1]);
    
    var disBtn = '#btn-dislike-' + ideaid;
    if (setOfVotes.votes[1] == 0) {
        $(disBtn).removeClass('active');
    } else {
        $(disBtn).addClass('active');
    }
}