package com.restoran.Interfase;

import com.restoran.Models.Products;

public interface IProduct{
	void OnSelectProduct(Products.Product p, Products.Product.Chast ch);
	void OnSelectProduct(Products.Product p, int gram);
	void OnSelectProduct(Products.Product p);
}