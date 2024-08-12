use master
go

create database coffee_shop
go

use coffee_shop
go

create table category
(
    id         int identity,
    name       nvarchar(255) unique not null,
    status     bit default (1),
    created_at datetime             not null,
    updated_at datetime,
    primary key (id)
)
go

create table product
(
    id          int identity,
    name        nvarchar(255) unique not null,
    price       float                not null,
    status      bit default (1),
    created_at  datetime             not null,
    updated_at  datetime             not null,
    category_id int                  not null,
    primary key (id),
    foreign key (category_id) references category (id)
)
go

create table bill
(
    id         int identity,
    status     bit default (1),
    created_at datetime not null,
    updated_at datetime,
    primary key (id)
)
go

create table bill_detail
(
    bill_id    int not null,
    product_id int not null,
    amount     int not null,
    primary key (bill_id, product_id),
    foreign key (bill_id) references bill (id),
    foreign key (product_id) references product (id)
)

go

create proc usp_insert_category(
    @_name nvarchar(255),
    @_status bit = 1,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if exists(select name from category where name = @_name)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Category exists!';
        end
    else
        begin
            begin tran;
            insert into category(name, status, created_at, updated_at)
            values (@_name, @_status, getdate(), getdate());
            set @_out_stt = 1;
            set @_out_msg = N'Insert successfully';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

-- Thêm danh mục
exec usp_insert_category @_name = N'Cà Phê Việt';

go

exec usp_insert_category @_name = N'Cà Phê Tây';

go

exec usp_insert_category @_name = N'Sinh Tố';

go

exec usp_insert_category @_name = N'Trà';

go

exec usp_insert_category @_name = N'Trái Cây';

go

exec usp_insert_category @_name = N'Sữa Chua';

go

exec usp_insert_category @_name = N'Khác';

go

create proc usp_update_category(
    @_id int,
    @_name nvarchar(255),
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if not exists(select * from category where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Category to update not valid';
        end
    else
        begin
            if exists(select * from category where name = @_name and id <> @_id)
                begin
                    set @_out_stt = 0;
                    set @_out_msg = N'Category name already in use';
                end
            else
                begin
                    begin tran;
                    update category
                    set name= @_name,
                        status = @_status,
                        updated_at = getdate()
                    where id = @_id;
                    set @_out_stt = 1;
                    set @_out_msg = N'update successully';
                    if @@trancount > 0
                        commit tran;
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_delete_category(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(max) = '' output
)
as
begin try
    if exists (select *
               from category
                        join product on product.category_id = category.id
               where category.id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'category has product'
        end
    else
        begin
            begin tran;
            delete category
            where id = @_id;
            set @_out_stt = 1;
            set @_out_msg = N'delete successfully';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go


create proc usp_get_all_category(
    @_name nvarchar(255) = null,
    @_status bit = null
)
as
begin
    declare @sql nvarchar(max) = N'select * from category where 1 = 1';
    if (@_name is not null)
        set @sql = concat(@sql, N' and name like ''%', @_name, N'%''');
    if (@_status is not null)
        set @sql = concat(@sql, N' and status = ', @_status);
    exec (@sql)
end
go



create proc usp_get_category_by_id(
    @_id int
) as
begin
    select top 1 * from category where id = @_id
end
go

create proc usp_insert_product(
    @_category_id INT,
    @_name NVARCHAR(100),
    @_price FLOAT,
    @_status BIT = 1,
    @_out_stt BIT = 1 OUTPUT,
    @_out_msg NVARCHAR(200) = '' OUTPUT
) as
begin try
    if not exists(select * from category where id = @_category_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = 'Category not exists!';
        end
    else
        begin
            if exists(select * from product where lower(name) = lower(@_name))
                begin
                    set @_out_stt = 0;
                    set @_out_msg = 'Product exists'
                end
            else
                begin
                    begin tran;
                    insert into product(name, status, price, category_id, created_at, updated_at)
                    values (@_name, @_status, @_price, @_category_id, getdate(), getdate());
                    set @_out_stt = 1;
                    set @_out_msg = 'Create successfully'
                    if @@trancount > 0
                        commit tran
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message()
    if @@trancount > 0
        rollback tran
end catch
go
EXEC usp_insert_product 1, N'Bạc xỉu', 35000;

GO

EXEC usp_insert_product 1, N'Cà phê sữa tươi', 35000;

GO

EXEC usp_insert_product 1, N'Cà phê đen', 29000;

GO

EXEC usp_insert_product 1, N'Cà phê nâu', 35000;

GO

EXEC usp_insert_product 1, N'Cà phê sữa tươi', 35000;

GO

EXEC usp_insert_product 2, N'Latte', 45000;

GO

EXEC usp_insert_product 2, N'Cappuchino', 45000;

GO

EXEC usp_insert_product 2, N'Espresso', 30000;

GO

EXEC usp_insert_product 3, N'Sinh tố bơ', 59000;

GO

EXEC usp_insert_product 3, N'Sinh tố xoài', 5000;

GO

EXEC usp_insert_product 4, N'Trà cam quế', 45000;

GO

EXEC usp_insert_product 4, N'Trà đào chanh leo', 45000;

GO

EXEC usp_insert_product 4, N'Trà quất mật ong', 45000;

GO

EXEC usp_insert_product 4, N'Trà lip ton', 25000;

GO

EXEC usp_insert_product 4, N'Trà mạn', 35000;

GO

EXEC usp_insert_product 5, N'Cóc xanh (theo mùa)', 55000;

GO

EXEC usp_insert_product 5, N'Chanh tươi', 39000;

GO

EXEC usp_insert_product 5, N'Dưa hấu', 49000;

GO

EXEC usp_insert_product 5, N'Chanh leo', 49000;

GO

EXEC usp_insert_product 5, N'Cam tươi', 65000;

GO

EXEC usp_insert_product 5, N'Ổi', 45000;

GO

EXEC usp_insert_product 5, N'Xoài xanh', 45000;

GO

EXEC usp_insert_product 6, N'Sữa chua dầm đá', 35000;

GO

EXEC usp_insert_product 6, N'Sữa chua ca cao', 40000;

GO

EXEC usp_insert_product 6, N'Sữa chua cà phê', 40000;

GO

EXEC usp_insert_product 6, N'Sữa chua trái cây', 55000;

GO

EXEC usp_insert_product 7, N'Hạt hướng dương', 25000;

GO

EXEC usp_insert_product 7, N'Lạc rang', 25000;

GO

EXEC usp_insert_product 7, N'Ngô cay', 25000;

GO

EXEC usp_insert_product 7, N'Bánh đậu xanh & Kẹo lạc', 25000;

GO

EXEC usp_insert_product 7, N'Bánh sừng bò chấm sữa', 25000;

GO

EXEC usp_insert_product 7, N'Thịt bò khô', 40000;

GO

create proc usp_update_product(
    @_id int,
    @_category_id INT,
    @_name NVARCHAR(100),
    @_price FLOAT,
    @_status BIT = 1,
    @_out_stt BIT = 1 OUTPUT,
    @_out_msg NVARCHAR(200) = '' OUTPUT
) as
begin try
    if not exists(select * from product where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = 'Product not exists';
        end
    else
        begin
            if exists(select * from product where name = @_name and id <> @_id)
                begin
                    set @_out_stt = 0;
                    set @_out_msg = 'Product name exists';
                end
            else
                begin
                    begin tran;
                    update product
                    set name= @_name,
                        status = @_status,
                        price = @_price,
                        category_id = @_category_id,
                        updated_at= getdate()
                    where id = @_id;
                    set @_out_stt = 1;
                    set @_out_msg = 'Update successfully!'
                    if @@trancount > 0
                        commit tran
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_delete_product(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
) as
begin try
    if exists(select *
              from product
                       join bill_detail on bill_detail.product_id = product.id
              where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = 'Product exists in some bills';
        end
    else
        begin
            begin tran;
            delete product
            where id = @_id;
            set @_out_stt = 1;
            set @_out_msg = 'Delete successfully';
            if @@trancount > 0
                commit tran
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message()
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_get_all_product(
    @_name nvarchar(100) = null,
    @_category_id int = null,
    @_fromPrice float = null,
    @_toPrice float = null,
    @_status bit = null
)
as
declare
    @sql NVARCHAR(MAX)
        = N'
		select p.*, c.[name] category_name
		from product p
		join category c
        on c.id = p.category_id where 1=1 and c.status = 1';

    if (@_name is not null)
        set @sql = concat(@sql, N' and p.name like ''%', @_name, N'%''');

    if (@_category_id is not null)
        set @sql = concat(@sql, N' and p.category_id = ', @_category_id);

    if (@_fromPrice is not null)
        set @sql = concat(@sql, N' and p.price >= ', @_fromPrice);

    if (@_toPrice is not null)
        set @sql = concat(@sql, N' and p.price <= ', @_toPrice);

    if (@_status is not null)
        set @sql = concat(@sql, N' and p.status = ', @_status);

    exec (@sql);

go

exec usp_get_all_product
go

create proc usp_get_product_by_id(
    @_id int
) as
begin
    select top 1 * from product where id = @_id
end
go

create proc usp_create_bill(
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar = '' output
)
as
begin try
    begin tran;
    insert into bill(status, created_at, updated_at) values (@_status, getdate(), getdate());
    set @_out_stt = 1;
    set @_out_msg = N'Create Bill successfully';
    if @@trancount > 0
        commit tran
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_update_bill(
    @_id int,
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if not exists(select * from bill where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Bill not exist';
        end
    else
        begin
            begin
                begin tran;
                update bill
                set status     = @_status,
                    updated_at = getdate()
                where id = @_id;

                set @_out_stt = 1;
                set @_out_msg = N'update successully';
                if @@trancount > 0
                    commit tran;
            end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_create_bill_detail(
    @_bill_id int,
    @_product_id int,
    @_amount int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar = '' output
)
as
begin try
    if not exists(select * from bill where id = @_bill_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Bill not exist';
        end
    else
        if not exists(select * from product where id = @_product_id)
            begin
                set @_out_stt = 0;
                set @_out_msg = N'Product not exist';
            end
        else
            begin
                if @_amount <= 0
                    begin
                        set @_out_stt = 0;
                        set @_out_msg = N'Amount not valid';
                    end
                else
                    begin
                        begin tran;
                        insert into bill_detail(bill_id, product_id, amount) values (@_bill_id, @_product_id, @_amount)
                        set @_out_stt = 1;
                        set @_out_msg = N'Create successfully';
                        if @@trancount > 0
                            commit tran;
                    end
            end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go
