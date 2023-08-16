package v1

import (
	"errors"
	"github.com/evrone/go-clean-template/internal"
	"github.com/evrone/go-clean-template/internal/entity"
	"github.com/evrone/go-clean-template/internal/usecase"
	"github.com/evrone/go-clean-template/pkg/logger"
	"github.com/gin-gonic/gin"
	"net/http"
)

type movieRoutes struct {
	m usecase.Movie
	l logger.Interface
}

func newMovieRoutes(handler *gin.RouterGroup, m usecase.Movie, l logger.Interface) {
	r := &movieRoutes{m, l}

	h := handler.Group("/movies")
	{
		h.POST("/index", r.doIndex)
		h.POST("/search", r.search)
		h.POST("/autocomplete", r.autoComplete)
		h.POST("/genre", r.getByGenre)
	}
}

type indexMovieRequest struct {
	ReleaseYear int      `json:"releaseYear" binding:"required"`
	Title       string   `json:"title" binding:"required"`
	Director    []string `json:"director" binding:"required"`
	Cast        []string `json:"cast" binding:"required"`
	Genre       []string `json:"genre" binding:"required"`
	Synopsis    string   `json:"synopsis" binding:"required"`
	Url         []string `json:"url" binding:"required"`
	Rating      float64  `json:"rating" binding:"required"`
	Image       string   `json:"image" binding:"required"`
}

// @Summary  create and index a document
// @Description create and index a document
// @ID          index
// @Tags  	    movies
// @Accept      json
// @Produce     json
// @Param       request body indexMovieRequest true "Set up movie"
// @Success     200 {object} movieResponse
// @Failure     500 {object} response
// @Router      /movies/index [post]
func (r *movieRoutes) doIndex(c *gin.Context) {
	var request indexMovieRequest
	if err := c.ShouldBindJSON(&request); err != nil {
		r.l.Error(err, "http - v1 - doIndex")
		errorResponse(c, http.StatusBadRequest, "invalid request body")
		return
	}

	err := r.m.Index(
		c.Request.Context(),
		entity.Movie{
			ReleaseYear: request.ReleaseYear,
			Title:       request.Title,
			Director:    request.Director,
			Cast:        request.Cast,
			Genre:       request.Genre,
			Synopsis:    request.Synopsis,
			Url:         request.Url,
			Rating:      request.Rating,
			Image:       request.Image,
		})
	if err != nil {
		r.l.Error(err, "http - v1 - doIndex")
		errorResponse(c, http.StatusInternalServerError, "movie service problems")

		return
	}
	c.JSON(http.StatusOK, "ok")
}

type searchRequest struct {
	ReleaseYear *[]int    `json:"rly" binding:"-"`
	Query       *string   `json:"q" binding:"required"`
	Director    *string   `json:"dir" binding:"-"`
	Cast        *[]string `json:"cast" binding:"-"`
	Genre       *[]string `json:"gnr" binding:"-"`
	MinRating   *float64  `json:"mrtg" binding:"-"`
	From        *int64    `json:"from" binding:"required"`
	Size        int64     `json:"size" binding:"required"`
}

type searchResponse struct {
	Movies []entity.Movie `json:"movies" binding:"required"`
}

// @Summary  search movies
// @Description search movies
// @ID          search
// @Tags  	    movies
// @Accept      json
// @Produce     json
// @Param       request body searchRequest true "Set up search"
// @Success     200 {object} searchResponse
// @Failure     500 {object} response
// @Router      /movies/search [post]
func (r *movieRoutes) search(c *gin.Context) {
	var req searchRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		r.l.Error(err, "http - v1 - search")
		errorResponse(c, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Size > 20 {
		errorResponse(c, http.StatusBadRequest, "invalid request body. size harus kurang dari 20")
		return
	}

	movies, err := r.m.Search(
		c.Request.Context(),
		entity.Search{
			ReleaseYear: req.ReleaseYear,
			Query:       req.Query,
			Director:    req.Director,
			Cast:        req.Cast,
			Genre:       req.Genre,
			MinRating:   req.MinRating,
			From:        req.From,
			Size:        req.Size,
		})
	if err != nil {
		var ierr *internal.Error
		r.l.Error(err, "http - v1 - search")
		if !errors.As(err, &ierr) {
			errorResponse(c, http.StatusInternalServerError, "movie service problems")
		} else {
			switch ierr.Code() {
			case internal.ErrorCodeNotFound:
				errorResponse(c, http.StatusNotFound, "movie service problems")
			case internal.ErrorCodeInvalidArgument:
				errorResponse(c, http.StatusBadRequest, "movie service problems")
			default:
				errorResponse(c, http.StatusInternalServerError, "movie service problems")
			}
		}
		return
	}
	c.JSON(http.StatusOK, searchResponse{movies})
}

type autoCompleteRequest struct {
	Word string `json:"word" binding:"-"`
}

// @Summary  autocomplete search as you type movies
// @Description autocomplete search as you type movies
// @ID          autocomplete
// @Tags  	    movies
// @Accept      json
// @Produce     json
// @Param       request body autoCompleteRequest true "Set up autocomplete"
// @Success     200 {object} searchResponse
// @Failure     500 {object} response
// @Router      /movies/autocomplete [post]
func (r *movieRoutes) autoComplete(c *gin.Context) {
	var req autoCompleteRequest
	if err := c.ShouldBindJSON(&req); err != nil {

		r.l.Error(err, "http - v1 - autoComplete")
		errorResponse(c, http.StatusBadRequest, "invalid request body")
		return
	}

	movies, err := r.m.AutoComplete(
		c.Request.Context(),
		entity.AutoComplete{
			Word: req.Word,
		},
	)
	if err != nil {
		var ierr *internal.Error
		r.l.Error(err, "http - v1 - autoComplete")
		if !errors.As(err, &ierr) {
			errorResponse(c, http.StatusInternalServerError, "movie service problems")
		} else {
			switch ierr.Code() {
			case internal.ErrorCodeNotFound:
				errorResponse(c, http.StatusNotFound, "movie service problems")
			case internal.ErrorCodeInvalidArgument:
				errorResponse(c, http.StatusBadRequest, "movie service problems")
			default:
				errorResponse(c, http.StatusInternalServerError, "movie service problems")
			}
		}
		return
	}
	c.JSON(http.StatusOK, searchResponse{movies})
}

type getByGenreRequest struct {
	Genre       *string `json:"genre" binding:"-"`
	ReleaseYear *[]int  `json:"rly" binding:"-"`
	From        *int64  `json:"from" binding:"-"`
	Size        int64   `json:"size" binding:"-"`
}

// @Summary  getByGenre  movies
// @Description getByGenre  movies
// @ID          getByGenre
// @Tags  	    movies
// @Accept      json
// @Produce     json
// @Param       request body getByGenre true "Set up genre"
// @Success     200 {object} searchResponse
// @Failure     500 {object} response
// @Router      /movies/genre [post]
func (r *movieRoutes) getByGenre(c *gin.Context) {
	var req getByGenreRequest
	if err := c.ShouldBindJSON(&req); err != nil {

		r.l.Error(err, "http - v1 - search")
		errorResponse(c, http.StatusBadRequest, "invalid request body")
		return
	}

	movies, err := r.m.GetByGenre(
		c.Request.Context(),
		entity.GetByGenre{
			Genre:       req.Genre,
			Size:        req.Size,
			From:        req.From,
			ReleaseYear: req.ReleaseYear,
		})
	if err != nil {
		var ierr *internal.Error
		r.l.Error(err, "http - v1 - getByGenre")
		if !errors.As(err, &ierr) {
			errorResponse(c, http.StatusInternalServerError, "movie service problems")
		} else {
			switch ierr.Code() {
			case internal.ErrorCodeNotFound:
				errorResponse(c, http.StatusNotFound, "movie service problems")
			case internal.ErrorCodeInvalidArgument:
				errorResponse(c, http.StatusBadRequest, "movie service problems")
			default:
				errorResponse(c, http.StatusInternalServerError, "movie service problems")
			}
		}
		return
	}

	c.JSON(http.StatusOK, searchResponse{movies})
}
